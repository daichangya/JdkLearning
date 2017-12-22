/*
 * @(#)FileSystemPreferences.java	1.11 01/12/03
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package java.util.prefs;
import java.util.*;
import java.io.*;
import java.util.logging.Logger;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;


/**
 * Preferences implementation for Unix.  Preferences are stored in the file
 * system, with one directory per preferences node.  All of the preferences
 * at each node are stored in a single file.  Atomic file system operations
 * (e.g. File.renameTo) are used to ensure integrity.  An in-memory cache of
 * the "explored" portion of the tree is maintained for performance, and 
 * written back to the disk periodically.  File-locking is used to ensure
 * reasonable behavior when multiple VMs are running at the same time.
 * (The file lock is obtained only for sync(), flush() and removeNode().)
 *
 * @author  Josh Bloch
 * @version 1.11, 12/03/01
 * @see     Preferences
 * @since   1.4
 */
class FileSystemPreferences extends AbstractPreferences {
    /**
     * Sync interval in seconds.
     */
    private static final int SYNC_INTERVAL = Math.max(1,
    Integer.parseInt(System.getProperty("java.util.prefs.syncInterval", "30")));

    /** 
     * Logger for error messages. Backing store exception are logged at
     * WARNING level.
     */
     private static final Logger logger = Logger.getLogger("java.util.prefs"); 

    /**
     * Directory for system preferences.
     */
    private static  File systemRootDir;
    
    static {
        // Temp fix until installation script issues get  resolved
        String systemPrefsDirName = (String)
          AccessController.doPrivileged( new PrivilegedAction() {
              public Object run() {
                  return System.getProperty
                          ("java.util.prefs.systemRoot","/etc/.java");
              }
        });
        
        systemRootDir = 
                     new File(systemPrefsDirName, ".systemPrefs");
    }

    static {
        AccessController.doPrivileged( new PrivilegedAction() {
            public Object run() {
            // Attempt to create root dir if it does not yet exist.
            // Temporary solution until install script issues get resolved
                if (!systemRootDir.exists()) {
                    if (systemRootDir.mkdirs()) {
                        logger.info("Recreated system preferences directory" 
                        + " in " + systemRootDir.getPath());
                    } else {
                        // Couldnt create systemRootDir
                        // Switching  to java.home as the last resort
                        systemRootDir = 
                                      new File(System.getProperty("java.home"), 
                                                                ".systemPrefs");
                        systemRootDir.mkdirs();
                    }
                }
                return null;
            }
        });
    }
        
    /*
     * Flag, indicating whether systemRoot  directory is writable
     */
    private static boolean isSystemRootWritable;
    
    static {
        AccessController.doPrivileged( new PrivilegedAction() {
            public Object run() {
                isSystemRootWritable = systemRootDir.canWrite();
                return null;
            }
        });
    }

    /**
     * Directory for user preferences.
     */
    private static File userRootDir;
     
    static { 
        AccessController.doPrivileged( new PrivilegedAction() {
            public Object run() {
                userRootDir = 
                      new File(System.getProperty("java.util.prefs.userRoot", 
                      System.getProperty("user.home")), ".java/.userPrefs");
                // Attempt to create root dir if it does not yet exist.        
                if (!userRootDir.exists()) {
                    userRootDir.mkdirs();
                    logger.warning("Recreated user preferences directory.");             
                }
                return null;
            }
        });
    }    
    /*
     * Flag, indicating whether userRoot  directory is writable
     */
    private static boolean isUserRootWritable;
    static {
        AccessController.doPrivileged( new PrivilegedAction() {
            public Object run() {
                isUserRootWritable = userRootDir.canWrite();
                return null;
            }
        });
    }    
     
    /**
     * The user root.
     */
    static FileSystemPreferences userRoot   = new FileSystemPreferences(true);

    /**
     * The system root.
     */
    static FileSystemPreferences systemRoot = new FileSystemPreferences(false);

    /** 
     * Unix user write/read permission
     */
    private static final int USER_READ_WRITE = 0600;
    
    /** 
     * Unix all write/read permission
     */
    private static final int ALL_READ_WRITE = 0666;
    
    /** 
     * Unix all read/write/execute permission
     */
    private static final int ALL_READ_WRITE_EXECUTE = 0777;
    
    /**
     * The lock file for the user tree.
     */
    static File userLockFile = new File
                        (userRootDir,".user.lock." + System.getProperty("user.name"));
    
    /**
     * The lock file for the system tree.
     */
    static File systemLockFile = new File(systemRootDir, ".system.lock");

    /**
     * Unix lock handle for userRoot.
     * Zero, if unlocked. 
     */

     private static long userRootLockHandle = 0;

    /** 
     * Unix lock handle for systemRoot.
     * Zero, if unlocked. 
     */

    private static long systemRootLockHandle = 0;

    /**
     * The directory representing this preference node.  There is no guarantee
     * that this directory exits, as another VM can delete it at any time
     * that it (the other VM) holds the file-lock.  While the root node cannot
     * be deleted, it may not yet have been created, or the underlying
     * directory could have been deleted accidentally.
     */
    private final File dir;

    /**
     * The file representing this preference node's preferences.
     * The file format is undocumented, and subject to change
     * from release to release, but I'm sure that you can figure
     * it out if you try real hard.
     */
    private final File prefsFile;

    /**
     * A temporary file used for saving changes to preferences.  As part of
     * the sync operation, changes are first saved into this file, and then
     * atomically renamed to prefsFile.  This results in an atomic state
     * change from one valid set of preferences to another.  The
     * the file-lock is held for the duration of this transformation.
     */
    private final File tmpFile;    
        
    /** 
     * File, which keeps track of global modifications of userRoot.
     */
    private static final File userRootModFile = 
        new File (userRootDir,".userRootModFile." + System.getProperty("user.name"));
    
    /** 
     * Flag, which indicated whether userRoot was modified by another VM
     */ 
    private static boolean isUserRootModified = false;

    /** 
     * Keeps track of userRoot modification time. This time is reset to
     * zero after UNIX reboot, and is increased by 1 second each time 
     * userRoot is modified.
     */
    private static long userRootModTime;
    
    static {
        AccessController.doPrivileged( new PrivilegedAction() {
            public Object run() {        
                if (!userRootModFile.exists())
                try {
                    // create if does not exist.                    
                    userRootModFile.createNewFile(); 
                    // Only user can read/write userRootModFile.
                    int result = chmod(userRootModFile.getCanonicalPath(), 
                                                               USER_READ_WRITE);
                    if (result !=0) 
                        logger.warning("Chmod failed on " + 
                               userRootModFile.getCanonicalPath() +
                              " Unix error code " + result);
                    } catch (IOException e) { logger.warning(e.toString());
                    }
                    userRootModTime = userRootModFile.lastModified();
                    return null;
            }
        });
    }
    
    
    /* 
     * File, which keeps track of global modifications of systemRoot
     */
    private static final File systemRootModFile = 
                                     new File (systemRootDir,".systemRootModFile");
    /* 
     * Flag, which indicates whether systemRoot was modified by another VM
     */
    private static boolean isSystemRootModified = false; 

    /** 
     * Keeps track of userRoot modification time. This time is reset to
     * zero after system reboot, and is increased by 1 second each time 
     * systemRoot is modified.
     */
    private static long systemRootModTime;    
    static {
        AccessController.doPrivileged( new PrivilegedAction() {
            public Object run() {                
                if (!systemRootModFile.exists() && isSystemRootWritable)
                try {
                    // create if does not exist.
                    systemRootModFile.createNewFile();
                    // Everybody can read/write systemRootModFile.
                    int result = chmod(systemRootModFile.getCanonicalPath(), 
                                                               ALL_READ_WRITE);
                    if (result !=0) 
                        logger.warning("Chmod failed on " + 
                               systemRootModFile.getCanonicalPath() +
                              " Unix error code " + result);
                } catch (IOException e) { logger.warning(e.toString());
                }
                systemRootModTime = systemRootModFile.lastModified();
                return null;
            }
        });
    }    
    
    /**
     * Locally cached preferences for this node (includes uncommitted
     * changes).  This map is initialized with from disk when the first get or
     * put operation occurs on this node.  It is synchronized with the
     * corresponding disk file (prefsFile) by the sync operation.  The initial
     * value is read *without* acquiring the file-lock.
     */
    private Map prefsCache = null;

    /**
     * The last modification time of the file backing this node at the time
     * that prefCache was last synchronized (or initially read).  This
     * value is set *before* reading the file, so it's conservative; the 
     * actual timestamp could be (slightly) higher.  A value of zero indicates
     * that we were unable to initialize prefsCache from the disk, or
     * have not yet attempted to do so.  (If prefsCache is non-null, it
     * indicates the former; if it's null, the latter.)
     */
    private long lastSyncTime = 0;

   /** 
    * Unix error code for locked file.
    */   
    private static final int EAGAIN = 11;

   /** 
    * Unix error code for denied access.
    */      
    private static final int EACCES = 13;
    
    /* Used to interpret results of native functions */
    private static final int LOCK_HANDLE = 0;
    private static final int ERROR_CODE = 1;
       
    /**
     * A list of all uncommitted preference changes.  The elements in this
     * list are of type PrefChange.  If this node is concurrently modified on
     * disk by another VM, the two sets of changes are merged when this node
     * is sync'ed by overwriting our prefsCache with the preference map last
     * written out to disk (by the other VM), and then replaying this change
     * log against that map.  The resulting map is then written back
     * to the disk.
     */
    final List changeLog = new ArrayList();        
    
    /**
     * Represents a change to a preference.
     */
    private abstract class Change {
        /**
         * Reapplies the change to prefsCache.
         */
        abstract void replay();
    };

    /**
     * Represents a preference put.
     */
    private class Put extends Change {
        String key, value;

        Put(String key, String value) {
            this.key = key;
            this.value = value;
        }

        void replay() {
            prefsCache.put(key, value);
        }
    }

    /**
     * Represents a preference remove.
     */
    private class Remove extends Change {
        String key;

        Remove(String key) {
            this.key = key;
        }

        void replay() {
            prefsCache.remove(key);
        }
    }

    /**
     * Represents the creation of this node.
     */
    private class NodeCreate extends Change {
        /**
         * Performs no action, but the presence of this object in changeLog
         * will force the node and its ancestors to be made permanent at the
         * next sync.
         */
        void replay() {
        }
    }

    /**
     * NodeCreate object for this node.
     */
    NodeCreate nodeCreate = null; 
    
    /**
     * Replay changeLog against prefsCache.
     */
    private void replayChanges() {
        for (int i = 0, n = changeLog.size(); i<n; i++)
            ((Change)changeLog.get(i)).replay();
    }

    private static Timer syncTimer = new Timer(true); // Daemon Thread

    static {
        // Add periodic timer task to periodically sync cached prefs
        syncTimer.schedule(new TimerTask() {
            public void run() {
                syncWorld();
            }
        }, SYNC_INTERVAL*1000, SYNC_INTERVAL*1000);

        // Add shutdown hook to flush cached prefs on normal termination
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                syncTimer.cancel();
                syncWorld();
            }
        });
    }

    private static void syncWorld() {
        try {
            Preferences.userRoot().flush();
        } catch(BackingStoreException e) {
            logger.warning("Couldn't flush user prefs: " + e);
        }

        try {
            Preferences.systemRoot().flush();
        } catch(BackingStoreException e) {
            logger.warning("Couldn't flush system prefs: " + e);
        }
    }

    /**
     * Special constructor for roots (both user and system).  This constructor
     * will only be called twice, by the static intializer.
     */
    private FileSystemPreferences(boolean user) {
        super(null, "");
        dir = (user ? userRootDir: systemRootDir);
        prefsFile = new File(dir, "prefs.xml");
        tmpFile   = new File(dir, "prefs.tmp");
    }

    /**
     * Construct a new FileSystemPreferences instance with the specified
     * parent node and name.  This constructor, called from childSpi,
     * is used to make every node except for the two //roots.
     */
    private FileSystemPreferences(FileSystemPreferences parent, String name) {
        super(parent, name);
        dir  = new File(parent.dir, dirName(name));
        prefsFile = new File(dir, "prefs.xml");
        tmpFile  = new File(dir, "prefs.tmp");
        AccessController.doPrivileged( new PrivilegedAction() {
            public Object run() {                
                newNode = !dir.exists();
                return null;
            }
        });
        if (newNode) {
            // These 2 things guarantee node will get wrtten at next flush/sync
            prefsCache = new TreeMap();
            nodeCreate = new NodeCreate();
            changeLog.add(nodeCreate);
        }
    }

    protected void putSpi(String key, String value) {
        initCacheIfNecessary();
        changeLog.add(new Put(key, value));
        prefsCache.put(key, value);
    }

    protected String getSpi(String key) {
        initCacheIfNecessary();
        return (String) prefsCache.get(key);
    }

    protected void removeSpi(String key) {
        initCacheIfNecessary();
        changeLog.add(new Remove(key));
        prefsCache.remove(key);
    }

    /**
     * Initialize prefsCache if it has yet to be initialized.  When this method
     * returns, prefsCache will be non-null.  If the data was successfully
     * read from the file, lastSyncTime will be updated.  If prefsCache was
     * null, but it was impossible to read the file (because it didn't
     * exist or for any other reason) prefsCache will be initialized to an
     * empty, modifiable Map, and lastSyncTime remain zero.
     */
    private void initCacheIfNecessary() {
        if (prefsCache != null)
            return;

        try {
            loadCache();
        } catch(Exception e) {
            // assert lastSyncTime == 0;
            prefsCache = new TreeMap();
        }
    }

    /**
     * Attempt to load prefsCache from the backing store.  If the attempt
     * succeeds, lastSyncTime will be updated (the new value will typically
     * correspond to the data loaded into the map, but it may be less,
     * if another VM is updating this node concurrently).  If the attempt
     * fails, a BackingStoreException is thrown and both prefsCache and
     * lastSyncTime are unaffected by the call.
     */
    private void loadCache() throws BackingStoreException {
        try {
            AccessController.doPrivileged( new PrivilegedExceptionAction() {
                public Object run() throws BackingStoreException {                
                    Map m = new TreeMap();
                    long newLastSyncTime = 0;
                    try {
                        newLastSyncTime = prefsFile.lastModified();
                        FileInputStream fis = new FileInputStream(prefsFile);
                        XmlSupport.importMap(fis, m);
                        fis.close();
                    } catch(Exception e) {
                        if (e instanceof InvalidPreferencesFormatException) {
                            logger.warning("Invalid preferences format in " +
                                                           prefsFile.getPath());
                            prefsFile.renameTo( new File(
                                                    prefsFile.getParentFile(),
                                                  "IncorrectFormatPrefs.xml"));
                            m = new TreeMap();
                        } else if (e instanceof FileNotFoundException) {
                            logger.warning(" Prefs file removed in background " 
                                           + prefsFile.getPath());
                        } else {
                            throw new BackingStoreException(e);
                        }
                    }
                    // Attempt succeeded; update state
                    prefsCache = m;
                    lastSyncTime = newLastSyncTime;
                    return null;
                }
            });
        } catch (PrivilegedActionException e) {
            throw (BackingStoreException) e.getException();
        }
    }

    /**
     * Attempt to write back prefsCache to the backing store.  If the attempt
     * succeeds, lastSyncTime will be updated (the new value will correspond
     * exactly to the data thust written back, as we hold the file lock, which
     * prevents a concurrent write.  If the attempt fails, a
     * BackingStoreException is thrown and both the backing store (prefsFile)
     * and lastSyncTime will be unaffected by this call.  This call will
     * NEVER leave prefsFile in a corrupt state.
     */
    private void writeBackCache() throws BackingStoreException {
        try {
            AccessController.doPrivileged( new PrivilegedExceptionAction() {
                public Object run() throws BackingStoreException {        
                    try {
                        if (!dir.exists() && !dir.mkdirs())                                 
                            throw new BackingStoreException(dir +
                                                             " create failed.");      
                        FileOutputStream fos = new FileOutputStream(tmpFile);
                        XmlSupport.exportMap(fos, prefsCache);
                        fos.close();
                        if (!tmpFile.renameTo(prefsFile))
                            throw new BackingStoreException("Can't rename " + 
                            tmpFile + " to " + prefsFile);
                    } catch(Exception e) {
                        if (e instanceof BackingStoreException)
                            throw (BackingStoreException)e;
                        throw new BackingStoreException(e);
                    }
                    return null;
                }
            });
        } catch (PrivilegedActionException e) {
            throw (BackingStoreException) e.getException();
        }                        
    }

    protected String[] keysSpi() {
        initCacheIfNecessary();
        return (String[])
            prefsCache.keySet().toArray(new String[prefsCache.size()]);
    }

    protected String[] childrenNamesSpi() {
        return (String[]) 
            AccessController.doPrivileged( new PrivilegedAction() {
                public Object run() {    
                    List result = new ArrayList();
                    File[] dirContents = dir.listFiles();
                    if (dirContents != null) {
                        for (int i = 0; i < dirContents.length; i++)
                            if (dirContents[i].isDirectory())
                                result.add(nodeName(dirContents[i].getName()));
                    }
                    return result.toArray(EMPTY_STRING_ARRAY);
               }
            });
    }

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    protected AbstractPreferences childSpi(String name) {
        return new FileSystemPreferences(this, name);
    }

    public void removeNode() throws BackingStoreException {
        synchronized (isUserNode()? userLockFile: systemLockFile) {
            if (!lockFile())
                throw(new BackingStoreException("Couldn't get file lock."));
           try {
                super.removeNode();
           } finally {
                unlockFile();
           }
        }
    }

    /**
     * Called with file lock held (in addition to node locks).
     */
    protected void removeNodeSpi() throws BackingStoreException {
        try {
            AccessController.doPrivileged( new PrivilegedExceptionAction() {
                public Object run() throws BackingStoreException {
                    if (changeLog.contains(nodeCreate)) {
                        changeLog.remove(nodeCreate);
                        nodeCreate = null;
                        return null;
                    }           
                    if (!dir.exists())
                        return null;
                    prefsFile.delete();
                    tmpFile.delete();
                    // dir should be empty now.  If it's not, empty it
                    File[] junk = dir.listFiles();
                    if (junk.length != 0) {
                        logger.warning("Found extraneous files when removing node: "
                        + Arrays.asList(junk));
                        for (int i=0; i<junk.length; i++)
                            junk[i].delete();
                    }
                    if (!dir.delete())
                        throw new BackingStoreException("Couldn't delete dir: " 
                                                                         + dir);
                    return null;
                }
            });
        } catch (PrivilegedActionException e) {
            throw (BackingStoreException) e.getException();
        }
    }
    
    public synchronized void sync() throws BackingStoreException {
        synchronized (isUserNode()? userLockFile:systemLockFile) {
           if (!lockFile())
               throw(new BackingStoreException("Couldn't get file lock."));
           final Long newModTime = 
                (Long) AccessController.doPrivileged( new PrivilegedAction() {
               public Object run() {
                   long nmt;
                   if (isUserNode()) {
                       nmt = userRootModFile.lastModified();
                       isUserRootModified = userRootModTime == nmt; 
                   } else {
                       nmt = systemRootModFile.lastModified();
                       isSystemRootModified = systemRootModTime == nmt; 
                   }
                   return new Long(nmt);
               }
           });
           try {
               super.sync();
               AccessController.doPrivileged( new PrivilegedAction() {
                   public Object run() {               
                   if (isUserNode()) {
                       userRootModTime = newModTime.longValue() + 1000;
                       userRootModFile.setLastModified(userRootModTime);
                   } else {
                       systemRootModTime = newModTime.longValue() + 1000;
                       systemRootModFile.setLastModified(systemRootModTime);
                   } 
                   return null;
                   }
               });
           } finally {
                unlockFile();
           }
        }
    }
    
    protected void syncSpi() throws BackingStoreException {
        try {
            AccessController.doPrivileged( new PrivilegedExceptionAction() {
                public Object run() throws BackingStoreException {
                    syncSpiPrivileged();
                    return null;
                }
            });
        } catch (PrivilegedActionException e) {
            throw (BackingStoreException) e.getException();
        }                        
    }
    private void syncSpiPrivileged() throws BackingStoreException {
        if (isRemoved())
            throw new IllegalStateException("Node has been removed"); 
        if (prefsCache == null)
            return;  // We've never been used, don't bother syncing
        long lastModifiedTime;
        if ((isUserNode() ? isUserRootModified : isSystemRootModified)) {
            lastModifiedTime = prefsFile.lastModified();
            if (lastModifiedTime  != lastSyncTime) {
                // Prefs at this node were externally modified; read in node and
                // playback any local mods since last sync
                loadCache();
                replayChanges();
                lastSyncTime = lastModifiedTime;
            } 
        } else if (lastSyncTime != 0 && !dir.exists()) {
            // This node was removed in the background.  Playback any changes
            // against a virgin (empty) Map.
            prefsCache = new TreeMap();
            replayChanges();
        }
        if (!changeLog.isEmpty()) {
            writeBackCache();  // Creates directory & file if necessary
           /*
            * Attempt succeeded; it's barely possible that the call to
            * lastModified might fail (i.e., return 0), but this would not
            * be a disaster, as lastSyncTime is allowed to lag.
            */
            lastModifiedTime = prefsFile.lastModified();
            /* If lastSyncTime did not change, or went back 
             * increment by 1 second. Since we hold the lock 
             * lastSyncTime always monotonically encreases in the
             * atomic sense.
             */
            if (lastSyncTime <= lastModifiedTime) {
                lastSyncTime = lastModifiedTime + 1000;
                prefsFile.setLastModified(lastSyncTime); 
            }
            changeLog.clear();
        }
    }

    public void flush() throws BackingStoreException {
        sync();
    }

    protected void flushSpi() throws BackingStoreException {
        // assert false;
    }

    /**
     * Returns true if the specified character is appropriate for use in
     * Unix directory names.  A character is appropriate if it's a printable
     * ASCII character (> 0x1f && < 0x7f) and unequal to slash ('/', 0x2f),
     * dot ('.', 0x2e), or underscore ('_', 0x5f).
     */
    private static boolean isDirChar(char ch) {
        return ch > 0x1f && ch < 0x7f && ch != '/' && ch != '.' && ch != '_';
    }

    /**
     * Returns the directory name corresponding to the specified node name.
     * Generally, this is just the node name.  If the node name includes
     * inappropriate characters (as per isDirChar) it is translated to Base64.
     * with the underscore  character ('_', 0x5f) prepended. 
     */
    private static String dirName(String nodeName) {
        for (int i=0, n=nodeName.length(); i < n; i++)
            if (!isDirChar(nodeName.charAt(i)))
                return "_" + Base64.byteArrayToAltBase64(byteArray(nodeName));
        return nodeName;
    }

    /**
     * Translate a string into a byte array by translating each character
     * into two bytes, high-byte first ("big-endian").
     */
    private static byte[] byteArray(String s) {
        int len = s.length();
        byte[] result = new byte[2*len];
        for (int i=0, j=0; i<len; i++) {
            char c = s.charAt(i);
            result[j++] = (byte) (c>>8);
            result[j++] = (byte) c;
        }
        return result;
    }

    /**
     * Returns the node name corresponding to the specified directory name.
 * (Inverts the transformation of dirName(String).
     */
    private static String nodeName(String dirName) {
        if (dirName.charAt(0) != '_')
            return dirName;
        byte a[] = Base64.altBase64ToByteArray(dirName.substring(1));
        StringBuffer result = new StringBuffer(a.length/2);
        for (int i = 0; i < a.length; ) {
            int highByte = a[i++] & 0xff;
            int lowByte =  a[i++] & 0xff;
            result.append((char) ((highByte << 8) | lowByte));
        }
        return result.toString();
    }

    /**
     * Try to acquire the appropriate file lock (user or system).  If
     * the initial attempt fails, several more attempts are made using
     * an exponential backoff strategy.  If all attempts fail, this method
     * returns false.
     * @throws SecurityException if file access denied.
     */
    private boolean lockFile() throws SecurityException{
        boolean usernode = isUserNode();
        // If we do not own prefs, we cant lock them.
        if (!(usernode? isUserRootWritable: isSystemRootWritable))
            return true;        
        int[] result;
        int errorCode = 0;
        File lockFile = (usernode ? userLockFile : systemLockFile);
        long sleepTime = INIT_SLEEP_TIME;
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            try {
                /* System prefs lock file should be available to everybody who
                   has a write permission on system prefs root. User prefs 
                   may be locked by user only. */
                  int perm = (usernode? USER_READ_WRITE:ALL_READ_WRITE);
                  result = lockFile0(lockFile.getCanonicalPath(), perm);
                  
                  errorCode = result[ERROR_CODE];
                  if (result[LOCK_HANDLE] != 0) {
                     if (usernode) {
                         userRootLockHandle = result[LOCK_HANDLE];
                     } else {
                     systemRootLockHandle = result[LOCK_HANDLE];
                     } 
                     return true;
                  }   
            } catch(IOException e) {
//                // If at first, you don't succeed...
            }

            try {
                Thread.sleep(sleepTime);
            } catch(InterruptedException e) {
                checkLockFile0ErrorCode(errorCode);
                return false;
            }
            sleepTime *= 2;
        }
        checkLockFile0ErrorCode(errorCode);
        return false;
    }
    
    /** 
     * Checks if unlockFile0() returned an error. Throws a SecurityException,
     * if access denied. Logs a warning otherwise.
     */
    private void checkLockFile0ErrorCode (int errorCode) 
                                                      throws SecurityException {
        if (errorCode == EACCES)
            throw new SecurityException("Could not lock" + 
            (isUserNode()? "User prefs." : "System prefs.") +
             "Lock file access denied.");
        if (errorCode != EAGAIN)
            logger.warning("Could not lock " + (isUserNode()? "User prefs. " :
                "System prefs.") + "Unix error code " + errorCode + ".");
    }             
        
    /**
     * Locks file using UNIX file locking.
     * @param fileName Absolute file name of the lock file.
     * @return Returns a lock handle, used to unlock the file.
     */
    private static native int[] lockFile0(String fileName, int permission);

    /**
     * Unlocks file previously locked by lockFile0().
     * @param lockHandle Handle to the file lock.
     * @return Returns zero if OK, UNIX error code if failure.
     */
    private  static native int unlockFile0(long lockHandle); 
    
    /**
     * Changes UNIX file permissions.
     */
    private static native int chmod(String fileName, int permission);
    
    /**
     * Initial time between lock attempts, in ms.  The time is doubled
     * after each failing attempt (except the first).
     */
    private static int INIT_SLEEP_TIME = 50;

    /**
     * Maximum number of lock attempts.
     */
    private static int MAX_ATTEMPTS = 5;

    /**
     * Release the the appropriate file lock (user or system).
     * @throws SecurityException if file access denied.
     */
    private void unlockFile() {
        // If we do not own prefs, we cant lock them
        boolean usernode = isUserNode();
        if (!(usernode? isUserRootWritable: isSystemRootWritable))
            return;
        int result;
        File lockFile = ( usernode ? userLockFile : systemLockFile);
        long lockHandle = ( usernode ? userRootLockHandle:systemRootLockHandle);
        if (lockHandle == 0) {
            logger.warning("Unlock: zero lockHandle for " + 
                           (usernode ? "user":"system") + " preferences.)"); 
            return;
        }
        result = unlockFile0(lockHandle);    
        if (result != 0) {
            logger.warning("Could not drop file-lock on " + 
            (isUserNode() ? "user" : "system") + " preferences." +
            "Unix error code " + result + ".");
            if (result == EACCES)
                throw new SecurityException("Could not unlock" + 
                (isUserNode()? "User prefs." : "System prefs.") +
                "Lock file access denied.");
        }        
        if (isUserNode()) {
            userRootLockHandle = 0;
        } else {
            systemRootLockHandle = 0;
        }
    }
}

package manic.com.popularmovies.database;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;
    private final ExecutorService execService;

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread, ExecutorService execService){
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
        this.execService = execService;
    }

    public static AppExecutors getInstance(){
        if(sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor(), Executors.newFixedThreadPool(3));
            }
        }
        return sInstance;
    }

    public Executor diskIO(){ return diskIO; }
    public Executor mainThread() { return mainThread; }
    public Executor networkIO() { return networkIO; }
    public ExecutorService execService(){
        return execService;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}

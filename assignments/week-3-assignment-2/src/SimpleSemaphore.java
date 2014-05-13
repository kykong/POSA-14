import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;

/**
 * @class SimpleSemaphore
 *
 * @brief This class provides a simple counting semaphore
 *        implementation using Java a ReentrantLock and a
 *        ConditionObject.  It must implement both "Fair" and
 *        "NonFair" semaphore semantics, just liked Java Semaphores. 
 */
public class SimpleSemaphore {


	/**
     * Constructor initialize the data members.  
     */
    public SimpleSemaphore (int permits,
                            boolean fair)
    { 
    	lock = new ReentrantLock(fair);
    	notZero = lock.newCondition();
    	this.permits = permits;
    }

    /**
     * Acquire one permit from the semaphore in a manner that can
     * be interrupted.
     */
    public void acquire() throws InterruptedException {
    	lock.lockInterruptibly();
    	try {
    		while (permits == 0) {
    			notZero.await();
    		}
    		permits--;
    		
    	} finally {
    		lock.unlock();
    	}
    }

    /**
     * Acquire one permit from the semaphore in a manner that
     * cannot be interrupted.
     */
    public void acquireUninterruptibly() {
        lock.lock();
    	try {
    		while (permits == 0) {
    			notZero.awaitUninterruptibly();
    		}
    		permits--;
    		
    	} finally {
    		lock.unlock();
    	}    
    }

    /**
     * Return one permit to the semaphore.
     */
    void release() {
        lock.lock();
        try {
        	permits++;
        	notZero.signal();
        	
        } finally {
        	lock.unlock();
        }
        
    }

    /**
     * Define a ReentrantLock to protect the critical section.
     */
    private ReentrantLock lock;

    /**
     * Define a ConditionObject to wait while the number of
     * permits is 0.
     */
	private Condition notZero;


    /**
     * Define a count of the number of available permits.
     */
	private int permits;
}


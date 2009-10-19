/*
 * (c) Copyright IBM Corporation 2008
 *
 * This file is part of XRX/C++ native layer implementation.
 */

#ifndef X10_RUNTIME_THREAD_H
#define X10_RUNTIME_THREAD_H

#include <x10aux/config.h>
#include <x10aux/ref.h>

#include <x10/lang/Ref.h>
#include <x10/lang/String.h>
#include <x10/lang/VoidFun_0_0.h>
#include <x10aux/serialization.h>

#include <pthread.h>

namespace x10 {
    namespace lang {
        class String;
        class Object;
    }
}

namespace x10 {
    namespace runtime {

        class Worker;
        
        // execution thread condition & associated lock pair
        typedef struct {
            pthread_cond_t cond;
            pthread_mutex_t mutex;
        } cond_mutex_t;

        // thread permit type
        typedef struct {
            pthread_cond_t cond;
            pthread_mutex_t mutex;
            x10_boolean permit;
        } permit_t;
        
       /************************************************************
        * A thread is a unit of execution in a place.
        * In a given place, we can have multiple threads of execution.
        *
        * A union of a subset of the functionality of java.lang.Thread
        * augmented with the park/unpark API of java.util.concurrent.
        * locks.LockSupport.
        */
        class Thread : public x10::lang::Ref {
        public:
            RTT_H_DECLS_CLASS;

            // [constructors] Allocates a new Thread object.
            static x10aux::ref<Thread> _make(x10aux::ref<x10::lang::VoidFun_0_0> task,
                                             x10aux::ref<x10::lang::String> name);
            
            x10aux::ref<Thread> _constructor(x10aux::ref<x10::lang::VoidFun_0_0> task,
                                             x10aux::ref<x10::lang::String> name) {
                this->x10::lang::Ref::_constructor();
                thread_init(task, name);
                return this;
            } 

            static const x10aux::serialization_id_t _serialization_id;

            virtual x10aux::serialization_id_t _get_serialization_id() { return _serialization_id; };

            virtual void _serialize_body(x10aux::serialization_buffer &buf, x10aux::addr_map &m);

            template<class T> static x10aux::ref<T> _deserializer(x10aux::deserialization_buffer &buf);

            virtual void _deserialize_body(x10aux::deserialization_buffer& buf);

            // destructor
            ~Thread();

            // Returns a reference to the currently executing thread object.
            static x10aux::ref<Thread> currentThread(void);

            /**
             * Causes this thread to begin execution; the XRX runtime
             * calls the run method of this thread.
             * The result is that two threads are running concurrently:
             * the current thread (which returns from the call to the
             * start method) and the other thread (which executes its
             * run method).
             * Throws IllegalThreadStateException, if the thread was
             * already started.
             */
            void start(void);

            /**
             * Waits forever for this thread to die.
             */
            void join(void);

            /**
             * Tests if this thread is alive.
             * A thread is alive if it has been started and has not yet died.
             */
            x10_boolean isAlive();

            /**
             * Interrupts this thread.
             */
            void interrupt();

            /**
             * Causes the currently executing thread to sleep (cease
             * execution) for the specified number of milliseconds.
             * The thread does not lose ownership of any monitors.
             * Throws InterruptedException, if any thread has interrupted
             * the current thread.
             */
            static void sleep(x10_long millis);

            /**
             * Causes the currently executing thread to sleep (cease
             * execution) for the specified number of milliseconds plus
             * the specified number of nanoseconds.
             * The thread does not lose ownership of any monitors.
             * Throws InterruptedException, if any thread has interrupted
             * the current thread.
             */
            static void sleep(x10_long millis, x10_int nanos);
            /**
             * Disables the current thread for thread scheduling purposes
             * unless the permit is available.
             * If the permit is available then it is consumed and the call
             * returns immediately; otherwise the current thread becomes
             * disabled for thread scheduling purposes and lies dormant
             * until one of three things happen:
             * 1) Some other thread invokes unpark with the current thread
             * as the target; or
             * 2) Some other thread interrupts the current thread; or
             * 3) The call spuriously (ie, for no reason) returns.
             * This method does not report which of these caused the method
             * to return.  Callers should re-check the conditions which
             * caused the thread to park in the first place.
             */
            static void park(void);

            /**
             * Disables the current thread for thread scheduling purposes,
             * for up to the specified waiting time, unless the permit is
             * available.
             * If the permit is available then it is consumed and the call
             * returns immediately; otherwise the current thread becomes
             * disabled for thread scheduling purposes and lies dormant
             * until one of four things happen:
             * 1) Some other thread invokes unpark with the current thread
             * as the target; or
             * 2) Some other thread interrupts the current thread; or
             * 3) The specified waiting time elapses; or
             * 4) The call spuriously (ie, for no reason) returns.
             * This method does not report which of these caused the method
             * to return.  Callers should re-check the conditions which
             * caused the thread to park in the first place.
             */
            static void parkNanos(x10_long nanos);

            /**
             * Makes available the permit for the given thread, if it was
             * not already available.  If the thread was blocked on park
             * then it will unblock.  Otherwise, its next call to park is
             * guaranteed not to block.  This operation is not guaranteed
             * to have any effect at all if the given thread has not been
             * started.
             */
            static void unpark(x10aux::ref<Thread> thread);

            // Returns the current worker.
            x10aux::ref<x10::runtime::Worker> worker(void);

            // Set the current worker.
            void worker(x10aux::ref<x10::runtime::Worker> worker);

            // Returns this thread's name.
            const x10aux::ref<x10::lang::String> getName(void);

            /**
             * Returns the identifier of this thread. The thread ID is
             * a positive long number generated when this thread was created.
             * The thread ID is unique and remains unchanged during its lifetime.
             */
            long getId();

            // Changes the name of this thread to be equal to the argument name.
            void setName(const x10aux::ref<x10::lang::String> name);

        protected:
            // Helper method to initialize a Thread object.
            void thread_init(x10aux::ref<x10::lang::VoidFun_0_0> task,
                             const x10aux::ref<x10::lang::String> name);
            // Thread start routine.
            static void *thread_start_routine(void *arg);
            // Clean-up routine for sleep method call.
            static void thread_sleep_cleanup(void *arg);
            // Dummy interrupt handler.
            static void intr_hndlr(int signo);
            // Thread permit initialization.
            static void thread_permit_init(permit_t *perm);
            // Thread permit finalization.
            static void thread_permit_destroy(permit_t *perm);
            // Thread permit cleanup handler.
            static void thread_permit_cleanup(void *arg);
            // Thread mapper cleanup handler.
            static void thread_mapper_cleanup(void *arg);

        private:
            // the current worker
            x10aux::ref<x10::runtime::Worker> __current_worker;
            // the current thread
            static x10aux::ref<Thread> __current_thread;
            // internal thread id counter (monotonically increasing only)
            static long __thread_cnt;
            // thread id
            long __thread_id;
            // thread name
            x10aux::ref<x10::lang::String> __thread_name;
            // body to be run
            x10aux::ref<x10::lang::VoidFun_0_0> __taskBody;
            // thread's pthread id
            // ??using __thread clashes with already existing identifier??
            pthread_t __xthread;
            // thread attributes
            pthread_attr_t __xthread_attr;
            // thread run flags
            x10_boolean __thread_already_started;
            x10_boolean __thread_running;
            // thread start condition & associated lock
            pthread_cond_t __thread_start_cond;
            pthread_mutex_t __thread_start_lock;
            // thread specific permit object
            permit_t __thread_permit;
            // pthread -> Thread mapping is maintained as thread specific data
            static pthread_key_t __thread_mapper;
            static x10_boolean __thread_mapper_inited;
        };

        template<class T> x10aux::ref<T> Thread::_deserializer(x10aux::deserialization_buffer &buf) {
            x10aux::ref<Thread> this_ = new (x10aux::alloc_remote<Thread>()) Thread();
            this_->_deserialize_body(buf);
            return this_;
        }
    }
}

#endif /* X10_RUNTIME_THREAD_H */

// vim:tabstop=4:shiftwidth=4:expandtab

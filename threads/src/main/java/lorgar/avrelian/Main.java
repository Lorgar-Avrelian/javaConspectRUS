package lorgar.avrelian;

import java.util.concurrent.*;

/**
 * @author Victor Tokovenko
 */
public class Main {
    static int counter = 0;
    static final Object lock = new Object();
    static final Object lock_1 = new Object();
    static final Object lock_2 = new Object();
    // устанавливаем ограничение на количество одновременно выполняемых потоков
    // устанавливаем флаг "справедливый" на true
    // метод aсquire() будет раздавать разрешения в порядке очереди
    private static final Semaphore semaphore = new Semaphore(2, true);
    //Создаём CountDownLatch на 2 условия
    private static final CountDownLatch countDownLatch = new CountDownLatch(2);
    // Инициализируем барьер на 2 потока и задачей, которая будет выполняться,
    // когда у барьера соберётся 2 потока, после чего они будут освобождены
    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
    // Создаём обменник, который будет обмениваться типом Integer
    private static final Exchanger<Integer> exchanger = new Exchanger<>();
    // Создаём фазер. При создании экземпляра Phaser из основного потока
    // передаём в качестве аргумента 1 - это эквивалентно вызову метода
    // register() из текущего потока
    private static final Phaser phaser = new Phaser(1);

    public static void main(String[] args) {
//        example1();
//        example2();
//        example3();
//        example4();
//        example5();
//        example6();
//        example7();
//        deadLockExample();
//        example();
//        exampleJoin();
//        semaphoreExample();
//        exampleCountDownLatch();
//        exampleCyclicBarrier();
//        exchangerExample();
        phaserExample();
    }

    private static void phaserExample() {
        doPhaserOperation(0);
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                // Регистрируем этот поток с помощью метода register()
                phaser.register();
                // Приостанавливаем выполнение потока, пока все
                // потоки-участники не выполнят данную фазу
                phaser.arriveAndAwaitAdvance();
                doPhaserOperation(1);
                doPhaserOperation(2);
                doPhaserOperation(3);
                // Снимаем поток с регистрации методом arriveAndDeregister()
                phaser.arriveAndDeregister();
            }
        };
        thread_1.start();
        //
        new Thread(() -> {
            // Регистрируем этот поток с помощью метода register()
            phaser.register();
            // Приостанавливаем выполнение потока, пока все
            // потоки-участники не выполнят данную фазу
            phaser.arriveAndAwaitAdvance();
            doPhaserOperation(4);
            doPhaserOperation(5);
            doPhaserOperation(6);
            // Снимаем поток с регистрации методом arriveAndDeregister()
            phaser.arriveAndDeregister();
        }).start();
        //
        // Приостанавливаем выполнение потока, пока все
        // потоки-участники не выполнят данную фазу
        phaser.arriveAndAwaitAdvance();
        doPhaserOperation(7);
        doPhaserOperation(8);
        doPhaserOperation(9);
        // Снимаем поток с регистрации методом arriveAndDeregister()
        phaser.arriveAndDeregister();
    }

    private static void doPhaserOperation(int i) {
        System.out.println("Operation " + i);
        // Сообщаем, что поток выполнил ещё одну фазу
        phaser.arrive();
        String s = "";
        // Приостанавливаем выполнение потока, пока все
        // потоки-участники не выполнят данную фазу
        phaser.arriveAndAwaitAdvance();
        for (int j = 0; j < 100_000; j++) {
            s += j + " ";
        }
        // Приостанавливаем выполнение потока, пока все
        // потоки-участники не выполнят данную фазу
        phaser.arriveAndAwaitAdvance();
    }

    private static void exchangerExample() {
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                doExchangerOperation(1);
                doExchangerOperation(2);
                doExchangerOperation(3);
            }
        };
        thread_1.start();
        //
        new Thread(() -> {
            doExchangerOperation(4);
            doExchangerOperation(5);
            doExchangerOperation(6);
        }).start();
        //
        doExchangerOperation(0);
        doExchangerOperation(7);
        doExchangerOperation(8);
        doExchangerOperation(9);
    }

    private static void doExchangerOperation(int i) {
        Integer result = Integer.valueOf(i);
        String s = "";
        for (int j = 0; j < 100_000; j++) {
            s += j + " ";
        }
        try {
            result = exchanger.exchange(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Operation " + i + " Got value from operation " + result);
    }

    private static void exampleCyclicBarrier() {
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                doCyclicBarrierOperation(1);
                doCyclicBarrierOperation(2);
                doCyclicBarrierOperation(3);
            }
        };
        thread_1.start();
        //
        new Thread(() -> {
            doCyclicBarrierOperation(4);
            doCyclicBarrierOperation(5);
            doCyclicBarrierOperation(6);
        }).start();
        //
        doCyclicBarrierOperation(0);
        doCyclicBarrierOperation(7);
        doCyclicBarrierOperation(8);
        doCyclicBarrierOperation(9);
    }

    private static void doCyclicBarrierOperation(int i) {
        // Для указания потоку о том что он достиг барьера, нужно вызвать метод await()
        // После этого данный поток блокируется, и ждет пока остальные потоки достигнут барьера
        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("Operation " + i);
        String s = "";
        for (int j = 0; j < 100_000; j++) {
            s += j + " ";
        }
    }

    private static void exampleCountDownLatch() {
        doCountDownLatchOperation(0);
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                doCountDownLatchOperation(1);
                doCountDownLatchOperation(2);
                doCountDownLatchOperation(3);
            }
        };
        thread_1.start();
        //
        new Thread(() -> {
            doCountDownLatchOperation(4);
            doCountDownLatchOperation(5);
            doCountDownLatchOperation(6);
        }).start();
        //
        doCountDownLatchOperation(7);
        doCountDownLatchOperation(8);
        doCountDownLatchOperation(9);
    }

    private static void semaphoreExample() {
        doSemaphoreOperation(0);
        //
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                doSemaphoreOperation(1);
                doSemaphoreOperation(2);
                doSemaphoreOperation(3);
            }
        };
        thread_1.start();
        //
        new Thread(() -> {
            doSemaphoreOperation(4);
            doSemaphoreOperation(5);
            doSemaphoreOperation(6);
        }).start();
        //
        doSemaphoreOperation(7);
        doSemaphoreOperation(8);
        doSemaphoreOperation(9);
    }

    private static void doSemaphoreOperation(int i) {
        //acquire() запрашивает доступ к следующему за вызовом этого метода блоку кода,
        //если доступ не разрешен, поток вызвавший этот метод блокируется до тех пор,
        //пока семафор не разрешит доступ
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Operation " + i);
        String s = "";
        for (int j = 0; j < 100_000; j++) {
            s += j + " ";
        }
        //release(), напротив, освобождает ресурс
        semaphore.release();
    }

    private static void exampleJoin() {
        doOperation(0);
        //
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                doOperation(1);
                doOperation(2);
                doOperation(3);
            }
        };
        thread_1.start();
        //
        Thread thread_2 = new Thread(() -> {
            doOperation(4);
            doOperation(5);
            doOperation(6);
        });
        thread_2.start();
        try {
            thread_1.join();
            thread_2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //
        doOperation(7);
        doOperation(8);
        doOperation(9);
    }

    private static void example() {
        doOperation(0);
        //
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                doOperation(1);
                doOperation(2);
                doOperation(3);
            }
        };
        thread_1.start();
        //
        Thread thread_2 = new Thread(() -> {
            doOperation(4);
            doOperation(5);
            doOperation(6);
        });
        try {
            thread_1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread_2.start();
        //
        doOperation(7);
        doOperation(8);
        doOperation(9);
    }

    private static void example1() {
        doOperation(0);
        doOperation(1);
        doOperation(2);
        doOperation(3);
        doOperation(4);
        doOperation(5);
        doOperation(6);
        doOperation(7);
        doOperation(8);
        doOperation(9);
    }

    private static void example2() {
        doOperation(0);
        //
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                doOperation(1);
                doOperation(2);
                doOperation(3);
            }
        };
        thread_1.start();
        //
        new Thread(() -> {
            doOperation(4);
            doOperation(5);
            doOperation(6);
        }).start();
        //
        doOperation(7);
        doOperation(8);
        doOperation(9);
    }

    private static void example3() {
        doOperation(0);
        //
        startDaemon(1);
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                doOperation(1);
                doOperation(2);
                doOperation(3);
            }
        };
        thread_1.start();
        //
        new Thread(() -> {
            doOperation(4);
            doOperation(5);
            doOperation(6);
        }).start();
        //
        doOperation(7);
        doOperation(8);
        doOperation(9);
    }

    private static void example4() {
        Integer count = 0;
        //
        doOperation(0);
        //
        Thread daemon = startDaemon(1);
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                doOperation(1);
                doOperation(2);
                doOperation(3);
            }
        };
        thread_1.start();
        //
        new Thread(() -> {
            doOperation(4);
            doOperation(5);
            doOperation(6);
        }).start();
        //
        doOperation(7);
        doOperation(8);
        doOperation(9);
        daemon.interrupt();
    }

    private static void example5() {
        doOperation(0);
        //
        Thread daemon = startDaemon(1);
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                if (!Thread.currentThread().isInterrupted()) {
                    doOperation(1);
                }
                if (!Thread.currentThread().isInterrupted()) {
                    doOperation(2);
                }
                if (!Thread.currentThread().isInterrupted()) {
                    doOperation(3);
                }
            }
        };
        thread_1.start();
        //
        Thread thread_2 = new Thread(() -> {
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }
            doOperation(4);
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }
            doOperation(5);
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }
            doOperation(6);
        });
        thread_2.start();
        //
        thread_1.interrupt();
        //
        doOperation(7);
        doOperation(8);
        thread_2.interrupt();
        doOperation(9);
        daemon.interrupt();
    }

    private static void example6() {
        doSynchronizedCounterOperation(0);
        //
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                if (!Thread.currentThread().isInterrupted()) {
                    doSynchronizedCounterOperation(1);
                }
                if (!Thread.currentThread().isInterrupted()) {
                    doSynchronizedCounterOperation(2);
                }
                if (!Thread.currentThread().isInterrupted()) {
                    doSynchronizedCounterOperation(3);
                }
            }
        };
        thread_1.start();
        //
        Thread thread_2 = new Thread(() -> {
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }
            doSynchronizedCounterOperation(4);
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }
            doSynchronizedCounterOperation(5);
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }
            doSynchronizedCounterOperation(6);
        });
        thread_2.start();
        //
        //
        doSynchronizedCounterOperation(7);
        doSynchronizedCounterOperation(8);
        doSynchronizedCounterOperation(9);
    }

    private static void example7() {
        doSynchronizedBlockCounterOperation(0);
        //
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                if (!Thread.currentThread().isInterrupted()) {
                    doSynchronizedBlockCounterOperation(1);
                }
                if (!Thread.currentThread().isInterrupted()) {
                    doSynchronizedBlockCounterOperation(2);
                }
                if (!Thread.currentThread().isInterrupted()) {
                    doSynchronizedBlockCounterOperation(3);
                }
            }
        };
        thread_1.start();
        //
        Thread thread_2 = new Thread(() -> {
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }
            doSynchronizedBlockCounterOperation(4);
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }
            doSynchronizedBlockCounterOperation(5);
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }
            doSynchronizedBlockCounterOperation(6);
        });
        thread_2.start();
        //
        //
        doSynchronizedBlockCounterOperation(7);
        doSynchronizedBlockCounterOperation(8);
        doSynchronizedBlockCounterOperation(9);
    }

    private static Thread startDaemon(int i) {
        Thread daemon = new Thread(() -> {
            while (true) {
                System.out.println("Daemon " + i + " running");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Daemon " + i + " interrupted");
                    break;
                }
            }
        });
        daemon.start();
        return daemon;
    }

    private static void doOperation(int i) {
        System.out.println("Operation " + i);
        String s = "";
//        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 100_000; j++) {
            s += j + " ";
//            sb.append(j + " ");
        }
//        System.out.println(s);
//        System.out.println(sb);
    }

    private static void doCountDownLatchOperation(int i) {
        countDownLatch.countDown(); // команда уменьшает счётчик на 1
        System.out.println("Operation " + i);
        String s = "";
        for (int j = 0; j < 100_000; j++) {
            s += j + " ";
        }
        countDownLatch.countDown(); // команда уменьшает счётчик на 1
        // счётчик становится равным нулю, и все ожидающие потоки одновременно разблокируются
        //
        // метод await() блокирует поток, вызвавший его, до тех пор, пока
        // счётчик CountDownLatch не станет равен 0
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void doSynchronizedCounterOperation(int i) {
        System.out.println("Operation " + i + " counter " + counter);
        String s = "";
        for (int j = 0; j < 100_000; j++) {
            s += j + " ";
        }
        counter++;
    }

    private static void doSynchronizedBlockCounterOperation(int i) {
        synchronized (lock) {
//        synchronized (Main.class) {
            System.out.println("Operation " + i + " counter " + counter);
            counter++;
        }
        String s = "";
        for (int j = 0; j < 100_000; j++) {
            s += j + " ";
        }
    }

    private static void deadLockExample() {
        doSynchronizedDeadLockOperation(0);
        //
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                if (!Thread.currentThread().isInterrupted()) {
                    doSynchronizedDeadLockOperation(1);
                }
                if (!Thread.currentThread().isInterrupted()) {
                    doSynchronizedDeadLockOperation(2);
                }
                if (!Thread.currentThread().isInterrupted()) {
                    doSynchronizedDeadLockOperation(3);
                }
            }
        };
        thread_1.start();
        //
        Thread thread_2 = new Thread(() -> {
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }
            doSynchronizedDeadLockOperation(4);
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }
            doSynchronizedDeadLockOperation(5);
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }
            doSynchronizedDeadLockOperation(6);
        });
        thread_2.start();
        //
        //
        doSynchronizedDeadLockOperation(7);
        doSynchronizedDeadLockOperation(8);
        doSynchronizedDeadLockOperation(9);
    }

    private static void doSynchronizedDeadLockOperation(int i) {
        synchronized (lock_1) {
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (lock_2) {
                System.out.println("Operation " + i + " counter " + counter);
                counter++;
            }
        }
        synchronized (lock_2) {
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String s = "";
            synchronized (lock_1) {
                for (int j = 0; j < 100_000; j++) {
                    s += j + " ";
                }
            }
        }
    }
}
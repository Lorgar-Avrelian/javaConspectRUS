package lorgar.avrelian.examples.threads;

import java.util.concurrent.Semaphore;

public class Main {
    static int counter = 0;
    static final Object lock = new Object();
    static final Object lock_1 = new Object();
    static final Object lock_2 = new Object();

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
        semaphoreExample();
    }

    private static void semaphoreExample() {
        // устанавливаем ограничение на количество одновременно выполняемых потоков
        // устанавливаем флаг "справедливый" на true
        // метод aсquire() будет раздавать разрешения в порядке очереди
        final Semaphore semaphore = new Semaphore(2, true);
        //
        doOperation(0);
        //
        Thread thread_1 = new Thread() {
            @Override
            public void run() {
                try {
                    //acquire() запрашивает доступ к следующему за вызовом этого метода блоку кода,
                    //если доступ не разрешен, поток вызвавший этот метод блокируется до тех пор,
                    //пока семафор не разрешит доступ
                    semaphore.acquire();
                    doOperation(1);
                    //release(), напротив, освобождает ресурс
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    semaphore.acquire();
                    doOperation(2);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    semaphore.acquire();
                    doOperation(3);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread_1.start();
        //
        new Thread(() -> {
            try {
                semaphore.acquire();
                doOperation(4);
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                semaphore.acquire();
                doOperation(5);
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                semaphore.acquire();
                doOperation(6);
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        //
        try {
            semaphore.acquire();
            doOperation(7);
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            semaphore.acquire();
            doOperation(8);
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            semaphore.acquire();
            doOperation(9);
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

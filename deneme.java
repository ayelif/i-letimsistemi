import java.util.Random;
import java.util.Scanner;

public class deneme {
    static int N, M, P;
    static int[][] A, B, C;
    static long[] threadZaman;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Lütfen A matrisinin satır sayısını (N) girin: ");
        N = scanner.nextInt();
        System.out.print("Lütfen A matrisinin sütun sayısını ve B matrisinin satır sayısını (M) girin: ");
        M = scanner.nextInt();
        System.out.print("Lütfen B matrisinin sütun sayısını (P) girin: ");
        P = scanner.nextInt();


        matrisOlustur();

        System.out.println("A matrisi:");
        matrisYazdir(A, N, M);
        System.out.println("B matrisi:");
        matrisYazdir(B, M, P);

        threadZaman = new long[N];
        long baslangicZamani = System.nanoTime();

        Thread[] threads = new Thread[N];
        for (int i = 0; i < N; i++) {
            threads[i] = new Thread(new Worker(i));
            threads[i].start();
        }

        for (int i = 0; i < N; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long bitisZamani = System.nanoTime();

        System.out.println("C matrisi:");
        matrisYazdir(C, N, P);

        System.out.println("İş parçacıklarının süreleri:");
        for (int i = 0; i < N; i++) {
            System.out.println("Thread " + i + ": " + threadZaman[i] + " ns");
        }

        long toplamIslemSuresi = (bitisZamani - baslangicZamani) / 1000000;
        System.out.println("Toplam işlem süresi: " + toplamIslemSuresi + " ms");
    }

    static void matrisOlustur() {
        A = new int[N][M];
        B = new int[M][P];
        C = new int[N][P];

        Random random = new Random();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                A[i][j] = random.nextInt(10);
            }
        }
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < P; j++) {
                B[i][j] = random.nextInt(10);
            }
        }
    }

    static void matrisYazdir(int[][] matrix, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            System.out.print("| ");
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("|");
        }
    }

    static class Worker implements Runnable {
        int satir;

        Worker(int satir) {
            this.satir = satir;
        }

        @Override
        public void run() {
            long baslangicZamani = System.nanoTime();
            satirHesapla(satir);
            long bitisZamani = System.nanoTime();
            threadZaman[satir] = bitisZamani - baslangicZamani;
        }

        static void satirHesapla(int satir) {
            for (int sutun = 0; sutun < P; sutun++) {
                int toplam = 0;
                for (int k = 0; k < M; k++) {
                    toplam += A[satir][k] * B[k][sutun];
                }
                C[satir][sutun] = toplam;
            }
        }
    }
}

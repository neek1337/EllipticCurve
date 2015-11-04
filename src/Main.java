import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int bitSize = scanner.nextInt();
        BigInteger i, p, h1 = null, h2 = null;
        ComplexNumber decomposition = null;
        do {
            p = generateP(bitSize);
            i = findingI1(p);
            decomposition = gcd(new ComplexNumber(p), new ComplexNumber(i, BigInteger.ONE));
            h1 = p.add(BigInteger.ONE).subtract(decomposition.getR().multiply(new BigInteger("2")).abs());
        } while (!check(h1, p));
        System.out.println(decomposition);
        System.out.println("p = " + p);
        System.out.println("I = " + i);
        System.out.println("Порядок группы = " + h1);
        BigInteger a = null;
        Pair<BigInteger, BigInteger> p0 = null;
        Pair<BigInteger, BigInteger> sum = null;
        do {
            do {
                p0 = new Pair<BigInteger, BigInteger>(new BigInteger(p.bitCount(), new Random()).mod(p.subtract(BigInteger.ONE)).add(BigInteger.ONE),
                        new BigInteger(p.bitCount(), new Random()).mod(p.subtract(BigInteger.ONE)).add(BigInteger.ONE));
                a = p0.getValue().multiply(p0.getValue())
                        .subtract(p0.getKey().multiply(p0.getKey()).
                                multiply(p0.getKey())).multiply(inverse(p, p0.getKey())).mod(p);
            } while (isQuadraticResidue(p.subtract(a), h1));
          /*  System.out.println("a = " + a);
            System.out.println("x0 = " + p0.getKey());
            System.out.println("y0 = " + p0.getValue());    */
            sum = new Pair<BigInteger, BigInteger>(BigInteger.ZERO, BigInteger.ZERO);

            for (int j = 0; j < h1.intValue(); j++) {
                sum = calcSum(sum, p0, p, a);
                if (sum == null) {
                    break;
                }
            }
        } while (sum != null);
        System.out.println("a = " + a);
        System.out.print("Генератор : x = " + p0.getKey());
        System.out.println(";y = " + p0.getValue());
        System.out.println("y^2 = x^3 + " + a + "*x");
        sum = new Pair<BigInteger, BigInteger>(BigInteger.ZERO, BigInteger.ZERO);
        PrintWriter printWriterX = new PrintWriter(new File("x.txt"));
        for (int j = 0; j < h1.intValue(); j++) {
            if (sum != null) {
                printWriterX.println(sum.getKey());
            }
            sum = calcSum(sum, p0, p, a);
        }

        PrintWriter printWriterY = new PrintWriter(new File("y.txt"));
        sum = new Pair<BigInteger, BigInteger>(BigInteger.ZERO, BigInteger.ZERO);

        System.out.println("_________________________________");
        for (int j = 0; j < h1.intValue(); j++) {
            if (sum != null) {
                printWriterY.println(sum.getValue());
            }
            sum = calcSum(sum, p0, p, a);
        }
        printWriterX.flush();
        printWriterY.flush();
    }


    public static BigInteger inverse(BigInteger p, BigInteger a) {
        return a.modPow(p.subtract(new BigInteger("2")), p);
    }

    public static Pair<BigInteger, BigInteger> calcSum(Pair<BigInteger, BigInteger> a1, Pair<BigInteger, BigInteger> a2, BigInteger p, BigInteger a) {
        BigInteger lambda = null;
        if (a1 == null) {
            return a2;
        }
        if (a2 == null) {
            return a1;
        }
        if (a1.equals(a2)) {
            lambda = new BigInteger("3").multiply(a1.getKey()).multiply(a1.getKey()).add(a).multiply(inverse(p, new BigInteger("2").multiply(a1.getValue())));
        } else if (a1.getKey().equals(a2.getKey())) {
            return null;
        } else {
            lambda = (a2.getValue().subtract(a1.getValue())).multiply(inverse(p, a2.getKey().subtract(a1.getKey()))).mod(p);
        }
        BigInteger x3 = lambda.multiply(lambda).subtract(a2.getKey()).subtract(a1.getKey()).mod(p);
        BigInteger y3 = lambda.multiply(a1.getKey().subtract(x3)).subtract(a1.getValue()).mod(p);
        return new Pair<BigInteger, BigInteger>(x3, y3);
    }


    public static Boolean check(BigInteger h, BigInteger p) {
        BigInteger[] a = h.divideAndRemainder(new BigInteger("4"));
        if (a[1].equals(BigInteger.ZERO) && a[0].isProbablePrime(200)) {
            return true;
        }
        return false;
    }

    public static BigInteger findingI1(BigInteger p) {
        BigInteger a = new BigInteger(p.bitCount(), new Random());
        do {
            a = new BigInteger(p.bitCount(), new Random());
        } while (isQuadraticResidue(a, p));
        return a.modPow(p.subtract(BigInteger.ONE).divide(new BigInteger("4")), p);
    }


    public static ComplexNumber gcd(ComplexNumber a, ComplexNumber b) {
        while (!b.getNorm().equals(BigInteger.ZERO)) {
            ComplexNumber mod = a.mod(b);
            a = b;
            b = mod;
        }
        return a;
    }


    public static Boolean isQuadraticResidue(BigInteger a, BigInteger p) {
        return a.modPow(p.subtract(BigInteger.ONE).divide(new BigInteger("2")), p).equals(BigInteger.ONE);
    }

    //генерируем простое число сравнимое с 1 по модулю 4
    public static BigInteger generateP(int bitCnt) {
        BigInteger p = new BigInteger(bitCnt, new Random());
        do {
            p = new BigInteger(bitCnt, new Random());
            p = p.subtract(p.mod(new BigInteger("4"))).add(BigInteger.ONE);
            while (!p.isProbablePrime(200)) {
                p = p.add(new BigInteger("4"));
            }
        } while (p.intValue() > Math.pow(2, bitCnt));
        return p;
    }
}

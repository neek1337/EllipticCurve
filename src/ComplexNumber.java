import java.math.BigInteger;

public class ComplexNumber {
    private BigInteger r;
    private BigInteger i;

    public ComplexNumber(BigInteger r, BigInteger i) {
        this.r = r;
        this.i = i;
    }

    public ComplexNumber(BigInteger r) {
        this.r = r;
        this.i = BigInteger.ZERO;
    }

    public BigInteger getR() {
        return r;
    }

    public BigInteger getI() {
        return i;
    }


    public ComplexNumber add(ComplexNumber val) {
        return new ComplexNumber(r.add(val.r), i.add(val.i));
    }

    public ComplexNumber subtract(ComplexNumber val) {
        return new ComplexNumber(r.subtract(val.r), i.subtract(val.i));
    }

    public ComplexNumber con() {
        return new ComplexNumber(r, i.multiply(new BigInteger("-1")));
    }

    public BigInteger getNorm() {
        return r.multiply(r).add(i.multiply(i));
    }

    public ComplexNumber multiply(ComplexNumber val) {
        return new ComplexNumber(r.multiply(val.r).subtract(i.multiply(val.i)), r.multiply(val.i).add(i.multiply(val.r)));
    }

    private static BigInteger div(BigInteger a, BigInteger d) {
        BigInteger[] dr = a.abs().divideAndRemainder(d.abs());
        if (a.signum() < 0) {
            if (dr[1].compareTo(d.subtract(dr[1])) < 0) {
                return dr[0].multiply(new BigInteger("-1"));
            } else {
                return dr[0].add(BigInteger.ONE).multiply(new BigInteger("-1"));
            }
        } else {
            if (dr[1].compareTo(d.subtract(dr[1])) < 0) {
                return dr[0];
            } else {
                return dr[0].add(BigInteger.ONE);
            }
        }
    }

    public ComplexNumber divide(ComplexNumber val) {
        ComplexNumber numerator = this.multiply(val.con());
        BigInteger denominator = val.multiply(val.con()).r;
        return new ComplexNumber(div(numerator.r, denominator), div(numerator.i, denominator));
    }

    public ComplexNumber mod(ComplexNumber val) {
        return this.subtract(this.divide(val).multiply(val));
    }

    @Override
    public String toString() {
        String result = "" + r;
        if (i.signum() > 0) {
            result += " + ";
        } else {
            result += " - ";
        }
        result += i.abs() + "i";
        return result;
    }
}

public class M2 {
    double a, b;
    double c, d;

    M2(double a, double b, double c, double d){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    V2 mul(V2 v){
        return new V2(a*v.x+b*v.y,
                      c*v.x+d*v.y);
    }
    M2 mul(M2 m){
        return new M2(a*m.a+b*m.c, a*m.b+b*m.d,
                      c*m.a+d*m.c, c*m.b+d*m.d);
    }
    M2 add(M2 m){
        return new M2(a+m.a, b+m.b,
                      c+m.c, d+m.d);
    }
    M2 sub(M2 m){    // matrix subtraction
        return new M2(this.a-m.a, this.b-m.b,
                this.c-m.c, this.d-m.d);
    }
    M2 mul(double k){    // scalar multiplication
        return new M2(a*k, b*k,
                c*k, d*k);
    }
    double det(){       // Determinant
        return a*d-b*c;
    }

    M2 adj(){           // Adjungatet Matrix
        return new M2( d,-b,
                -c, a);
    }
    M2 inv(){           // Inverse
        if (det()==0) throw new ArithmeticException("Matrix "+this+" has no inverse.");
        return adj().mul(1.0/det());
    }



    @Override
    public String toString() {
        return "M2{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                '}';
    }
}

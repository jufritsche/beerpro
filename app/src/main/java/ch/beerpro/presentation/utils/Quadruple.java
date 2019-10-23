package ch.beerpro.presentation.utils;

public class Quadruple<A, B, C, D> {
    private final A left;
    private final B leftMiddle;
    private final C rightMiddle;
    private final D right;

    public Quadruple(A left, B leftMiddle, C rightMiddle, D right) {
        this.left = left;
        this.leftMiddle = leftMiddle;
        this.rightMiddle = rightMiddle;
        this.right = right;
    }

    public static <A, B, C, D> Quadruple<A, B, C, D> of(final A left, B leftMiddle, C rightMiddle, D right) {
        return new Quadruple<>(left, leftMiddle, rightMiddle, right);
    }

    public A getLeft() {
        return this.left;
    }

    public B getLeftMiddle() {
        return this.leftMiddle;
    }

    public C getRightMiddle() {
        return rightMiddle;
    }

    public D getRight() {
        return right;
    }
}

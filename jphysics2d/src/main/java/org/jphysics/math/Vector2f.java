/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphysics.math;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.NumberFormat;
import org.joml.Vector2dc;
import org.joml.Vector2fc;

/**
 *
 * @author luis
 */
public class Vector2f extends org.joml.Vector2f {

    public Vector2f() {
    }

    public Vector2f(float d) {
        super(d);
    }

    public Vector2f(float x, float y) {
        super(x, y);
    }

    public Vector2f(Vector2fc v) {
        super(v);
    }

    public Vector2f(ByteBuffer buffer) {
        super(buffer);
    }

    public Vector2f(int index, ByteBuffer buffer) {
        super(index, buffer);
    }

    public Vector2f(FloatBuffer buffer) {
        super(buffer);
    }

    public Vector2f(int index, FloatBuffer buffer) {
        super(index, buffer);
    }

    public Vector2f rotateRad(float radians) {
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        float newX = this.x * cos - this.y * sin;
        float newY = this.x * sin + this.y * cos;

        this.x = newX;
        this.y = newY;

        return this;
    }

    public Vector2f setLength(float len) {
        return setLength2(len * len);
    }

    public Vector2f setLength2(float len2) {
        float oldLen2 = len2();
        return (oldLen2 == 0 || oldLen2 == len2) ? this : mul((float) Math.sqrt(len2 / oldLen2));
    }

    public float len2() {
        return x * x + y * y;
    }

    /**
     * Sets the angle of the vector in degrees relative to the x-axis, towards
     * the positive y-axis (typically counter-clockwise).
     *
     * @param degrees The angle in degrees to set.
     * @return
     */
    public Vector2f setAngle(float degrees) {
        return setAngleRad(degrees * MathUtils.degreesToRadians);
    }

    /**
     * Sets the angle of the vector in radians relative to the x-axis, towards
     * the positive y-axis (typically counter-clockwise).
     *
     * @param radians The angle in radians to set.
     * @return
     */
    public Vector2f setAngleRad(float radians) {
        this.set(length(), 0f);
        this.rotateRad(radians);

        return this;
    }

    @Override
    public Vector2fc toImmutable() {
        return super.toImmutable();
    }

    @Override
    public Vector2f fma(float a, Vector2fc b, org.joml.Vector2f dest) {
        return (Vector2f) super.fma(a, b, dest);
    }

    @Override
    public Vector2f fma(Vector2fc a, Vector2fc b, org.joml.Vector2f dest) {
        return (Vector2f) super.fma(a, b, dest);
    }

    @Override
    public Vector2f fma(float a, Vector2fc b) {
        return (Vector2f) super.fma(a, b);
    }

    @Override
    public Vector2f fma(Vector2fc a, Vector2fc b) {
        return (Vector2f) super.fma(a, b);
    }

    @Override
    public String toString(NumberFormat formatter) {
        return super.toString(formatter);
    }

    @Override
    public Vector2f lerp(Vector2fc other, float t, org.joml.Vector2f dest) {
        return (Vector2f) super.lerp(other, t, dest);
    }

    @Override
    public Vector2f lerp(Vector2fc other, float t) {
        return (Vector2f) super.lerp(other, t);
    }

    @Override
    public Vector2f mul(Vector2fc v, org.joml.Vector2f dest) {
        return (Vector2f) super.mul(v, dest);
    }

    @Override
    public Vector2f mul(Vector2fc v) {
        return (Vector2f) super.mul(v);
    }

    @Override
    public Vector2f mul(float x, float y, org.joml.Vector2f dest) {
        return (Vector2f) super.mul(x, y, dest);
    }

    @Override
    public Vector2f mul(float x, float y) {
        return (Vector2f) super.mul(x, y);
    }

    @Override
    public Vector2f mul(float scalar, org.joml.Vector2f dest) {
        return (Vector2f) super.mul(scalar, dest);
    }

    @Override
    public Vector2f mul(float scalar) {
        return (Vector2f) super.mul(scalar);
    }

    @Override
    public Vector2f negate(org.joml.Vector2f dest) {
        return (Vector2f) super.negate(dest);
    }

    @Override
    public Vector2f negate() {
        return (Vector2f) super.negate();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
    }

    @Override
    public Vector2f zero() {
        super.zero();
        return this;
    }

    @Override
    public Vector2f add(float x, float y, org.joml.Vector2f dest) {
        super.add(x, y, dest);
        return this;
    }

    @Override
    public Vector2f add(float x, float y) {
        return (Vector2f) super.add(x, y);
    }

    @Override
    public Vector2f add(Vector2fc v, org.joml.Vector2f dest) {
        return (Vector2f) super.add(v, dest);
    }

    @Override
    public Vector2f add(Vector2fc v) {
        return (Vector2f) super.add(v);
    }

    @Override
    public Vector2f normalize(org.joml.Vector2f dest) {
        return (Vector2f) super.normalize(dest);
    }

    @Override
    public Vector2f normalize() {
        return (Vector2f) super.normalize();
    }

    @Override
    public float distanceSquared(float x, float y) {
        return super.distanceSquared(x, y);
    }

    @Override
    public float distance(float x, float y) {
        return super.distance(x, y);
    }

    @Override
    public float distanceSquared(Vector2fc v) {
        return super.distanceSquared(v);
    }

    @Override
    public float distance(Vector2fc v) {
        return super.distance(v);
    }

    @Override
    public float lengthSquared() {
        return super.lengthSquared();
    }

    @Override
    public float length() {
        return super.length();
    }

    @Override
    public float angle(Vector2fc v) {
        return super.angle(v);
    }

    @Override
    public float dot(Vector2fc v) {
        return super.dot(v);
    }

    @Override
    public Vector2f sub(float x, float y, org.joml.Vector2f dest) {
        return (Vector2f) super.sub(x, y, dest);
    }

    @Override
    public Vector2f sub(float x, float y) {
        return (Vector2f) super.sub(x, y);
    }

    @Override
    public Vector2f sub(Vector2fc v, org.joml.Vector2f dest) {
        return (Vector2f) super.sub(v, dest);
    }

    @Override
    public Vector2f sub(Vector2fc v) {
        return (Vector2f) super.sub(v);
    }

    @Override
    public Vector2f perpendicular() {
        return (Vector2f) super.perpendicular();
    }

    @Override
    public FloatBuffer get(int index, FloatBuffer buffer) {
        return super.get(index, buffer);
    }

    @Override
    public FloatBuffer get(FloatBuffer buffer) {
        return super.get(buffer);
    }

    @Override
    public ByteBuffer get(int index, ByteBuffer buffer) {
        return super.get(index, buffer);
    }

    @Override
    public ByteBuffer get(ByteBuffer buffer) {
        return super.get(buffer);
    }

    @Override
    public Vector2f setComponent(int component, float value) throws IllegalArgumentException {
        return (Vector2f) super.setComponent(component, value);
    }

    @Override
    public Vector2f set(int index, FloatBuffer buffer) {
        return (Vector2f) super.set(index, buffer);
    }

    @Override
    public Vector2f set(FloatBuffer buffer) {
        return (Vector2f) super.set(buffer);
    }

    @Override
    public Vector2f set(int index, ByteBuffer buffer) {
        return (Vector2f) super.set(index, buffer);
    }

    @Override
    public Vector2f set(ByteBuffer buffer) {
        return (Vector2f) super.set(buffer);
    }

    @Override
    public Vector2f set(Vector2dc v) {
        return (Vector2f) super.set(v);
    }

    @Override
    public Vector2f set(Vector2fc v) {
        return (Vector2f) super.set(v);
    }

    @Override
    public Vector2f set(float x, float y) {
        return (Vector2f) super.set(x, y);
    }

    @Override
    public Vector2f set(float d) {
        return (Vector2f) super.set(d);
    }

    @Override
    public float y() {
        return super.y();
    }

    @Override
    public float x() {
        return super.x();
    }

}

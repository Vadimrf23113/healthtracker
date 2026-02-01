package com.healthtracker.util;

public final class Validators {

    private Validators() {
    }

    public static Double parseDoubleOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        try {
            return Double.parseDouble(t.replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer parseIntOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static boolean inRange(Integer v, int min, int max) {
        if (v == null) return true;
        return v >= min && v <= max;
    }

    public static boolean inRange(Double v, double min, double max) {
        if (v == null) return true;
        return v >= min && v <= max;
    }

    public static boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
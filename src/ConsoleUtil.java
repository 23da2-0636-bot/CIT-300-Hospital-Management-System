import java.util.List;

/**
 * ConsoleUtil
 * ------------------------------------------------------------
 * A small helper class that gives the whole application a
 * consistent, "boxed" professional console look using Unicode
 * box-drawing characters. Nothing here affects the data
 * structure logic — it purely formats output.
 * ------------------------------------------------------------
 */
public class ConsoleUtil {

    private static final int WIDTH = 64;

    // ANSI colors (safe to ignore if terminal doesn't support them)
    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BOLD = "\u001B[1m";

    /** Prints a top border with a centered title, e.g. ╔══ TITLE ══╗ */
    public static void printHeader(String title) {
        System.out.println(CYAN + "╔" + "═".repeat(WIDTH - 2) + "╗" + RESET);
        System.out.println(CYAN + "║" + RESET + BOLD + center(title, WIDTH - 2) + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚" + "═".repeat(WIDTH - 2) + "╝" + RESET);
    }

    /** Prints a full boxed block of text lines, e.g. a message card. */
    public static void printBox(String... lines) {
        System.out.println(CYAN + "┌" + "─".repeat(WIDTH - 2) + "┐" + RESET);
        for (String line : lines) {
            System.out.println(CYAN + "│ " + RESET + padRight(line, WIDTH - 4) + CYAN + " │" + RESET);
        }
        System.out.println(CYAN + "└" + "─".repeat(WIDTH - 2) + "┘" + RESET);
    }

    public static void success(String msg) {
        printBox(GREEN + "✔ " + msg + RESET);
    }

    public static void error(String msg) {
        printBox(RED + "✘ " + msg + RESET);
    }

    public static void info(String msg) {
        printBox(YELLOW + "ℹ " + msg + RESET);
    }

    /** Prints a simple divider line. */
    public static void divider() {
        System.out.println(CYAN + "─".repeat(WIDTH) + RESET);
    }

    /** Prints a formatted table for a list of table rows (String[] per row). */
    public static void printTable(String[] headers, List<String[]> rows) {
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) widths[i] = headers[i].length();
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                widths[i] = Math.max(widths[i], row[i].length());
            }
        }

        printTableBorder(widths, "┌", "┬", "┐");
        printTableRow(headers, widths, true);
        printTableBorder(widths, "├", "┼", "┤");
        if (rows.isEmpty()) {
            System.out.println(YELLOW + "  (no records to display)" + RESET);
        } else {
            for (String[] row : rows) printTableRow(row, widths, false);
        }
        printTableBorder(widths, "└", "┴", "┘");
    }

    private static void printTableBorder(int[] widths, String left, String mid, String right) {
        StringBuilder sb = new StringBuilder(CYAN + left);
        for (int i = 0; i < widths.length; i++) {
            sb.append("─".repeat(widths[i] + 2));
            sb.append(i == widths.length - 1 ? right : mid);
        }
        sb.append(RESET);
        System.out.println(sb);
    }

    private static void printTableRow(String[] cells, int[] widths, boolean isHeader) {
        StringBuilder sb = new StringBuilder(CYAN + "│" + RESET);
        for (int i = 0; i < cells.length; i++) {
            String text = isHeader ? BOLD + padRight(cells[i], widths[i]) + RESET
                                    : padRight(cells[i], widths[i]);
            sb.append(" ").append(text).append(" ").append(CYAN + "│" + RESET);
        }
        System.out.println(sb);
    }

    private static String padRight(String s, int n) {
        if (s.length() >= n) return s.substring(0, n);
        return s + " ".repeat(n - s.length());
    }

    private static String center(String s, int width) {
        if (s.length() >= width) return s.substring(0, width);
        int left = (width - s.length()) / 2;
        int right = width - s.length() - left;
        return " ".repeat(left) + s + " ".repeat(right);
    }
}

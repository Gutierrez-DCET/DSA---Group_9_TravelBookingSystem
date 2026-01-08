package com.mycompany.TBS;

import java.util.*;

public class BookingBST {
    class Node {
        String date;
        List<String> bookingIds = new ArrayList<>();
        Node left, right;
        Node(String d, String id) { date = d; bookingIds.add(id); }
    }
    private Node root;

    public void insert(String date, String id) { root = insertRec(root, date, id); }
    private Node insertRec(Node root, String date, String id) {
        if (root == null) return new Node(date, id);
        int cmp = date.compareTo(root.date);
        if (cmp < 0) root.left = insertRec(root.left, date, id);
        else if (cmp > 0) root.right = insertRec(root.right, date, id);
        else root.bookingIds.add(id);
        return root;
    }

    public List<String> search(String date) { return searchRec(root, date); }
    private List<String> searchRec(Node root, String date) {
        if (root == null) return null;
        if (root.date.equals(date)) return root.bookingIds;
        return date.compareTo(root.date) < 0 ? searchRec(root.left, date) : searchRec(root.right, date);
    }
    public void clear() { root = null; }
}
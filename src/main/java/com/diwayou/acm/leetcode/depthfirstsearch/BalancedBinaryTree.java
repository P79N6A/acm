package com.diwayou.acm.leetcode.depthfirstsearch;// Given a binary tree, determine if it is height-balanced.

// For this problem, a height-balanced binary tree is defined as a binary tree in which the depth of the two subtrees of every node never differ by more than 1.

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 * int val;
 * TreeNode left;
 * TreeNode right;
 * TreeNode(int x) { val = x; }
 * }
 */
public class BalancedBinaryTree {
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    boolean balanced = true;

    public boolean isBalanced(TreeNode root) {
        height(root);
        return balanced;
    }

    private int height(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int leftHeight = height(root.left);
        int rightHeight = height(root.right);

        if (Math.abs(leftHeight - rightHeight) > 1) {
            balanced = false;
        }

        return 1 + Math.max(leftHeight, rightHeight);
    }
}

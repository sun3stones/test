package com.lei.algorithm;

/**
 * 合并两个有序列表
 */
public class MergeLinkedList {

    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    private static ListNode merge(ListNode node1, ListNode node2){
        if (node1 == null && node2 == null){
            return null;
        }
        if (node1 == null){
            return node2;
        }
        if (node2 == null){
            return node1;
        }
        ListNode node;
        if (node1.val <= node2.val){
            node = node1;
            node.next = merge(node1.next,node2);
        } else {
            node = node2;
            node.next = merge(node1,node2.next);
        }
        return node;
    }

    private static void print(ListNode listNode){
        do {
            System.out.println(listNode.val);
            listNode = listNode.next;
        } while (listNode != null);
    }


    public static void main(String[] args) {
        ListNode listNode1 = new ListNode(1);
        listNode1.next = new ListNode(3);
        listNode1.next.next = new ListNode(5);
        listNode1.next.next.next = new ListNode(6);
        ListNode listNode2 = new ListNode(2);
        listNode2.next = new ListNode(4);
        listNode2.next.next = new ListNode(7);
        listNode2.next.next.next = new ListNode(8);
        ListNode listNode = merge(listNode1,listNode2);
        print(listNode);
    }
}

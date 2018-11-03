package com.example.gamal.myapplication;

import android.util.Log;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Arrays;
import java.util.Scanner;

import static com.example.gamal.myapplication.Game.ai_moves;

public class Solution {
    //priority queue is created for holding all the state objects created.
// Array List 'expanded' is created for holding all expanded states information.
    public static PriorityQueue<State> pq;
    public static ArrayList<State> expanded;
    public static String[][] goal = {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", ""}};
    String a[][];//initial state

    //Below constructor of Solution Class is initiated by the State class object 'first'
    public Solution(State first) {
        if (first == null) {
//            System.out.println("Please provide an input");
        }
        pq = new PriorityQueue<>();
        expanded = new ArrayList<>();
        pq.add(first);
        ArrayList<State> list = new ArrayList<State>();
        while (!pq.isEmpty()) {
            State current = pq.poll();    //returns and deletes the first node of the priority queue and store it in 'current' variable.
            expanded.add(current);            //Adds current object to the 'end' list<State> which holds all the expanded nodes
            if (Arrays.deepEquals(current.blocks, goal)) {
                break;
            }
            list = current.expand(current); //expands the current node and the child nodes are stored in the list<State>
//Below code verify whether the node expanded is already visited by verifying in the 'end' array list
            for (int i = 0; i < list.size(); i++) {
                if (checkIfExpandedStatesAreNotInExpanded(list.get(i)) && checkIfExpandedStatesAreNotInQueue(list.get(i))) {
                    pq.add(list.get(i));
                }
            }

        }
    }


    private boolean checkIfExpandedStatesAreNotInExpanded(State s) {
        for (State e : pq) {
            if (s.blocks.equals(e))
                return false;
            if (Arrays.deepEquals(s.blocks, e.blocks)) {
                return false;
            }

        }
        return true;
    }

    private boolean checkIfExpandedStatesAreNotInQueue(State s) {
        for (State e : expanded) {
            if (s.blocks.equals(e))
                return false;
            if (Arrays.deepEquals(s.blocks, e.blocks)) {
                if (s.f < e.f) {
                    e.f = s.f;
                    e.MOVE = s.MOVE;
                    e.level = s.level;
                    e.parentPath = s.parentPath;
                }
                return false;
            }
        }
        return true;
    }


    public void set_a(String a[][]) {
        this.a = a;
    }

    public Solution() {

    }

    public void releaseResources() {
        pq.clear();
        pq = null;
        expanded.clear();
        expanded = null;
    }

    public void test() {
        long startTime = System.currentTimeMillis();
        State state = new State(a, 0);
        new Solution(state);
        StringBuilder stringBuilder = new StringBuilder();
        State lastOne = null;
        for (State states : expanded) {
            for (int l = 0; l < 3; l++) {
                for (int m = 0; m < 3; m++) {
                    stringBuilder.append(states.blocks[l][m] + ",");
                }

            }
            Log.e("xx", stringBuilder.toString());
            stringBuilder.setLength(0);
            lastOne = states;
        }
        Log.e("xx", lastOne.parentPath.toString());
        ai_moves = lastOne.parentPath;
    }
}
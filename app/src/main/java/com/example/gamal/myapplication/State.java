package com.example.gamal.myapplication;

import android.util.Log;

import java.util.ArrayList;

public class State implements Comparable<State> {
    public int f;
    public String[][] blocks;
    public int level;
    String MOVE = "";
    ArrayList<String> parentPath = new ArrayList<>();

    public State(String[][] a, int level) {
        int N = a.length;
        this.blocks = new String[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.blocks[i][j] = a[i][j];
            }
        }

        this.level = level;
        this.f = manhattan() + level;
    }

    /*Below function calculates the Manhattan distance(heuristic value)	for each state or node.
        I.e the sum of the distances of the tiles from their goal positions*/
    private int manhattan() {
        int sum = 0;
        int[] index = new int[2];
        int N = Solution.goal.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.blocks[i][j].trim().isEmpty()) {
                    continue;
                }
                index = find_index(Integer.parseInt(this.blocks[i][j]));
                sum = sum + (Math.abs(i - index[0]) + Math.abs(j - index[1]));
            }
        }
        return sum;
    }

    //Below method find the indices of a particular element in the goal state and return them in an array.
    private int[] find_index(int a) {
        int[] index = new int[2];
        int N = Solution.goal.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (Solution.goal[i][j].trim().isEmpty()) {
                    continue;
                }
                if (Solution.goal[i][j].trim().equals(String.valueOf(a))) {
                    index[0] = i;
                    index[1] = j;
                    return index;
                }
            }
        }
        return index;
    }

    //Below method generates all the possible child nodes from a given parent node.
    public ArrayList<State> expand(State parent) {
        ArrayList<State> successor = new ArrayList<State>();
        int N = this.blocks.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (parent.blocks[i][j].trim().isEmpty()) //search for the index of space in the state(where a tile can be moved)
                {
                    if (i - 1 >= 0)            //checks where a tile can be moved towards the top.
                    {
                        String[][] a = new String[N][N];
                        for (int l = 0; l < N; l++) {
                            for (int m = 0; m < N; m++) {
                                a[l][m] = parent.blocks[l][m];
                            }
                        }
                        a = swap(a, i, j, i - 1, j);
                        State b = new State(a, parent.level + 1);
                        b.MOVE = "UP";
                        for (int x = 0; x < this.parentPath.size(); x++) {
                            b.parentPath.add(this.parentPath.get(x));
                        }
                        b.parentPath.add(b.MOVE);
                        successor.add(b);
                    }
                    if (j - 1 >= 0)            //checks whether a tile can be moved towards left of the space.
                    {
                        String[][] a = new String[N][N];
                        for (int l = 0; l < N; l++) {
                            for (int m = 0; m < N; m++) {
                                a[l][m] = parent.blocks[l][m];
                            }
                        }
                        a = swap(a, i, j, i, j - 1);
                        State b = new State(a, parent.level + 1);
                        b.MOVE = "LEFT";
                        for (int x = 0; x < this.parentPath.size(); x++) {
                            b.parentPath.add(this.parentPath.get(x));
                        }
                        b.parentPath.add(b.MOVE);
                        successor.add(b);
                    }
                    if (i + 1 < N)            //checks whether a tile can be moved towards downward.
                    {
                        String[][] a = new String[N][N];
                        for (int l = 0; l < N; l++) {
                            for (int m = 0; m < N; m++) {
                                a[l][m] = parent.blocks[l][m];
                            }
                        }
                        a = swap(a, i, j, i + 1, j);
                        State b = new State(a, parent.level + 1);
                        b.MOVE = "DOWN";
                        for (int x = 0; x < this.parentPath.size(); x++) {
                            b.parentPath.add(this.parentPath.get(x));
                        }
                        b.parentPath.add(b.MOVE);
                        successor.add(b);

                    }
                    if (j + 1 < N)            //checks whether a tile can be moved towards right side.
                    {
                        String[][] a = new String[N][N];
                        for (int l = 0; l < N; l++) {
                            for (int m = 0; m < N; m++) {
                                a[l][m] = parent.blocks[l][m];
                            }
                        }
                        a = swap(a, i, j, i, j + 1);
                        State b = new State(a, parent.level + 1);
                        b.MOVE = "RIGHT";
                        for (int x = 0; x < this.parentPath.size(); x++) {
                            b.parentPath.add(this.parentPath.get(x));
                        }
                        b.parentPath.add(b.MOVE);
                        successor.add(b);
                    }
                }
            }
        }
        return successor;
    }


    //Below method is for swapping the desired elements in the indices of the blocks provided
    private String[][] swap(String[][] a, int row1, int col1, int row2, int col2) {
        String[][] copy = a;
        String tmp = copy[row1][col1];
        copy[row1][col1] = copy[row2][col2];
        copy[row2][col2] = tmp;

        return copy;
    }

    //Below function provide the sorting technique for the priority queue created in Solution class
    @Override
    public int compareTo(State o) {
        if (this.f == o.f) {
            return ((this.manhattan() - o.manhattan()));
        }
        return this.f - o.f;
    }
}

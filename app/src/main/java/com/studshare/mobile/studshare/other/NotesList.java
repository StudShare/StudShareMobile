package com.studshare.mobile.studshare.other;

public class NotesList {

    private static int ChosenID = 0;
    private static int[] List;

    public void clear() {
        List = null;
    }

    public int getChosenID() {
        return ChosenID;
    }

    public void setChosenID(int chosenID) {
        ChosenID = chosenID;
    }

    public void add(int index, int value) {
        List[index] = value;
    }

    public int getItem(int index) {
        return List[index];
    }

    public void setList(int[] list) {
        List = list;
    }
}

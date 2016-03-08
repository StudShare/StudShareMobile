package com.studshare.mobile.studshare.activity;


public class Save_text {

    public static String cont="";
    public static String getCont() { return cont; }
    public static void setCont(String login) { cont = login; }

   public static String type_note="photo";
}


//  String ext="text";
//   String text_from_note = Save_text.getLogin();
/*
    public void tryAdd(View view) {

        String title = txtTitle.getText().toString();
        String tags = txtTags.getText().toString();

        if (title.trim().equals("") || tags.trim().equals("")) {
            ShowMessage.Show(getApplicationContext(), "Proszę uzupełnić wszystkie pola");
            return;
        }


        ProfileManager.OperationStatus result;


        if (ext.equals("text"))
            result = noteManager.add2(title, text_from_note, ext);

        else {

            result = noteManager.add2(title, text_from_note, ext);

        }

        if (result == ProfileManager.OperationStatus.OtherError) {
            ShowMessage.Show(getApplicationContext(), "Wystąpił błąd dodawania NOTATKIII do bazy. Proszę sprawdzić połączenie internetowe.");
        }

        Intent goToNextActivity = new Intent(getApplicationContext(), MainScreenActivity.class);
        startActivity(goToNextActivity);
    }
*/
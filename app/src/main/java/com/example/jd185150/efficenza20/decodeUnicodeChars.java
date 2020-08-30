package com.example.jd185150.efficenza20;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class decodeUnicodeChars {
    public decodeUnicodeChars() {
    }

    public String giveMeDecodedMessage(String input) throws UnsupportedEncodingException {
        String newinput = "";
        //newinput = newinput.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        String result = java.net.URLDecoder.decode(input, "UTF-8");
        Log.d("Tady Je Decoder", input + " >>>>>>>>>> " + result);
        return result;
    }

    public SpannableStringBuilder RiplejsEmoji(Context context, String message) {
        final SpannableStringBuilder msg = new SpannableStringBuilder(message);
        Log.d("RiplejsEmoji", "message = " + message);
        Log.d("RiplejsEmoji", "msg = " + msg);
        /* nahrad ceske znaky */
        while (msg.toString().contains("&Scaron;")) {
            int location = msg.toString().indexOf("&Scaron;");
            msg.replace(location, location+8,"Š");
        }
        Log.d("RiplejsEmoji", "...passed Š");

        while (msg.toString().contains("&scaron;")) {
            int location = msg.toString().indexOf("&scaron;");
            msg.replace(location, location+8,"š");
        }
        Log.d("RiplejsEmoji", "...passed š");

        while (msg.toString().contains("&ntilde;")) {
            int location = msg.toString().indexOf("&ntilde;");
            msg.replace(location, location+8,"ñ");
        }
        Log.d("RiplejsEmoji", "...passed ň");

        while (msg.toString().contains("&Uacute;")) {
            int location = msg.toString().indexOf("&Uacute;");
            msg.replace(location, location+8,"Ú");
        }
        Log.d("RiplejsEmoji", "...passed Ú");

        while (msg.toString().contains("&aacute;")) {
            int location = msg.toString().indexOf("&aacute;");
            msg.replace(location, location+8, "á");
        }
        Log.d("RiplejsEmoji", "...passed á");

        while (msg.toString().contains("&iacute;")) {
            int location = msg.toString().indexOf("&iacute;");
            msg.replace(location, location+8, "í");
        }
        Log.d("RiplejsEmoji", "...passed í");

        while (msg.toString().contains("&oacute;")) {
            int location = msg.toString().indexOf("&oacute;");
            msg.replace(location, location+8, "ó");
        }
        Log.d("RiplejsEmoji", "...passed ó");

        while (msg.toString().contains("&uacute;")) {
            int location = msg.toString().indexOf("&uacute;");
            msg.replace(location, location+8, "ú");
        }
        Log.d("RiplejsEmoji", "...passed ú");

        while (msg.toString().contains("&Aacute;")) {
            int location = msg.toString().indexOf("&Aacute;");
            msg.replace(location, location+8, "Á");
        }
        Log.d("RiplejsEmoji", "...passed Á");

        while (msg.toString().contains("&yacute;")) {
            int location = msg.toString().indexOf("&yacute;");
            msg.replace(location, location+8, "ý");
        }
        Log.d("RiplejsEmoji", "...passed ý");

        while (msg.toString().contains("&eacute;")) {
            int location = msg.toString().indexOf("&eacute;");
            msg.replace(location, location+8, "é");
        }
        Log.d("RiplejsEmoji", "...passed é");
        /* ceske znaky ends */

        if(msg.toString().contains("@")) {
            int location = msg.toString().indexOf("@");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof @ :: " + location);
            msg.replace(location, location+1, " ");
        }

        if(msg.toString().contains("=-)")) {
            //laughing to cry
            int indOf = msg.toString().indexOf("=-)");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :-D :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley1);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains(":-P")) {
            //wink
            int indOf = msg.toString().indexOf(":-P");
            Log.d("--EMOJI", "message: " + msg.toString());
            Log.d("--EMOJI", "indexof :-) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley2);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains(":-(")) {
            //sad
            int indOf = msg.toString().indexOf(":-(");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof ;-) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley3);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("[-(")) {
            //dunno man!
            int indOf = msg.toString().indexOf("[-(");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :-( :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley4);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("|-(")) {
            //close to crying
            int indOf = msg.toString().indexOf("|-(");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :-* :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley5);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains(":=)")) {
            //yummy!
            int indOf = msg.toString().indexOf(":=)");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :-P :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley6);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("[-|")) {
            //Luis de Funes semsemikoukejte
            int indOf = msg.toString().indexOf("[-|");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof 8-) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley7);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains(":-[")) {
            //Luis de Funes tamsekoukejte
            int indOf = msg.toString().indexOf(":-[");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley8);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("8-O")) {
            //in awe!
            int indOf = msg.toString().indexOf("8-O");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley9);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("=O")) {
            //crying hard
            int indOf = msg.toString().indexOf("=O");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley10);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("*NO")) {
            //#11 nope man
            int indOf = msg.toString().indexOf("*NO");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley11);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("*OK")) {
            //OK
            int indOf = msg.toString().indexOf("*OK");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley13);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("X-X")) {
            //sick
            int indOf = msg.toString().indexOf("X-X");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley12);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains(":-/")) {
            //sad
            int indOf = msg.toString().indexOf(":-/");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley14);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("*SRY")) {
            //I am sorry
            int indOf = msg.toString().indexOf("*SRY");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley15);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains(":||")) {
            //shutting up *zipped
            int indOf = msg.toString().indexOf(":||");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley16);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains(":-S")) {
            //sssssh!!!!
            int indOf = msg.toString().indexOf(":-S");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley17);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("P-/")) {
            //facepalm
            int indOf = msg.toString().indexOf("P-/");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley18);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("3-(")) {
            //angry
            int indOf = msg.toString().indexOf("3-(");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley19);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("[[)")) {
            //hehehe
            int indOf = msg.toString().indexOf("[[)");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley20);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("[-L")) {
            //tongue out
            int indOf = msg.toString().indexOf("[-L");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley21);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains(":-E")) {
            //vysmatej
            int indOf = msg.toString().indexOf(":-E");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley22);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("8oO")) {
            //waaaat???
            int indOf = msg.toString().indexOf("8oO");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley23);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        //bonusovy
        if(msg.toString().contains(":~Z")) {
            //Zleman
            int indOf = msg.toString().indexOf(":~Z");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley24);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if(msg.toString().contains("[-]")) {
            //Pitscha
            int indOf = msg.toString().indexOf("[-]");
            Log.d("--EMOJI", "message: " + message);
            Log.d("--EMOJI", "indexof :) :: " + indOf);
            Drawable android = context.getResources().getDrawable(R.drawable.smiley25);
            android.setBounds(0, 0, android.getIntrinsicWidth(), android.getIntrinsicHeight());
            ImageSpan image = new ImageSpan(android, ImageSpan.ALIGN_BASELINE);
            msg.setSpan(image, indOf, indOf+3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        return msg;
    }
}

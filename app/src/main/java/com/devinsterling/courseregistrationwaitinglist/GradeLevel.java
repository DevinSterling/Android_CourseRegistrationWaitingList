package com.devinsterling.courseregistrationwaitinglist;
/* 
    Devin Sterling
    2022 - 07 - 26
    Course Registration Waiting List
*/

public class GradeLevel {
    /* Helps turn integers (0-5) from the db into more readable strings*/
    public static String getStringGradeLevel(int level) {
        switch (level) {
            case 1:
                return "1ˢᵗ Year: Freshman";
            case 2:
                return "2ⁿᵈ Year: Sophomore";
            case 3:
                return "3ʳᵈ Year: Junior";
            case 4:
                return "4ᵗʰ Year: Senior";
            case 5:
                return "5ᵗʰ Year: Graduate";
            default:
                return "Null";
        }
    }
}



package x10.parser;

import com.ibm.lpg.*;

class X10Lexerprs implements ParseTable, X10Lexersym {

    public interface IsKeyword {
        public final static byte isKeyword[] = {0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0
        };
    };
    public final static byte isKeyword[] = IsKeyword.isKeyword;
    public final boolean isKeyword(int index) { return isKeyword[index] != 0; }

    public interface BaseCheck {
        public final static byte baseCheck[] = {0,
            1,3,3,1,1,1,1,5,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,2,2,2,2,2,
            2,2,2,2,2,2,2,2,2,3,
            2,2,3,1,3,2,2,3,3,2,
            1,2,2,2,3,3,2,3,2,2,
            0,1,2,2,2,0,2,1,2,1,
            2,2,2,3,2,3,3,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,2,3,1,1,1,1,1,1,
            1,1,1,1,1,2,1,2,2,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,6,2,1,1,1,1,1,6,2,
            2,2,2,2,2,2,2,2,1,2,
            2,3
        };
    };
    public final static byte baseCheck[] = BaseCheck.baseCheck;
    public final int baseCheck(int index) { return baseCheck[index]; }
    public final static byte rhs[] = baseCheck;
    public final int rhs(int index) { return rhs[index]; };

    public interface BaseAction {
        public final static char baseAction[] = {
            17,17,17,17,17,17,17,17,17,17,
            17,17,17,17,17,17,17,17,17,17,
            17,17,17,17,17,17,17,17,17,17,
            17,17,17,17,17,17,17,17,17,17,
            17,17,17,17,17,17,17,17,17,17,
            17,17,17,17,21,21,22,23,23,23,
            23,24,24,24,24,24,24,24,25,25,
            25,25,26,26,27,27,19,19,7,7,
            30,30,32,32,32,12,12,12,10,10,
            10,10,10,4,4,4,4,4,5,5,
            5,5,5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,5,5,
            5,5,5,5,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,6,6,
            1,1,1,1,1,1,1,1,1,1,
            11,11,11,11,11,11,11,11,3,3,
            3,3,3,3,3,3,3,3,3,3,
            2,2,35,35,35,8,8,9,9,31,
            31,14,14,29,29,28,28,18,18,18,
            36,36,36,36,36,36,36,36,36,36,
            36,36,36,36,36,36,36,36,36,36,
            36,36,36,36,36,36,36,36,36,15,
            15,15,15,15,15,15,15,15,15,15,
            15,15,15,15,15,15,15,15,15,15,
            15,15,15,15,15,15,15,37,37,37,
            37,37,37,37,37,37,37,37,37,37,
            37,37,37,37,37,37,37,37,37,37,
            37,37,37,37,37,38,38,38,38,38,
            38,38,38,38,38,38,38,38,38,38,
            38,38,38,38,38,38,38,38,38,38,
            38,38,38,13,13,13,13,41,41,33,
            33,33,33,33,33,33,33,34,34,34,
            34,34,34,34,20,20,20,20,20,20,
            20,16,16,16,16,16,16,16,16,17,
            17,17,39,308,533,989,956,532,532,532,
            443,610,199,534,1163,198,198,198,96,1171,
            356,361,58,65,355,5,6,7,1,70,
            436,413,70,70,70,450,56,401,70,1031,
            408,70,349,70,510,530,408,198,530,530,
            530,410,77,1167,415,77,77,77,1070,79,
            530,59,66,894,530,103,68,77,942,68,
            68,68,378,196,1166,68,1020,470,68,463,
            68,1162,530,470,628,77,205,75,77,1165,
            75,75,75,924,79,684,476,476,476,1172,
            986,60,67,340,1173,403,1192,426,57,64,
            445,1193,364,1042,426,708,480,480,480,75,
            1109,79,75,1164,516,476,732,81,81,81,
            756,488,488,488,780,492,492,492,804,496,
            496,496,828,500,500,500,852,339,339,339,
            876,504,504,504,900,332,332,332,1059,520,
            1087,522,1098,528,977,520,1153,522,1168,528,
            1120,79,1131,79,975,333,1196,184,1142,79,
            383,507,962,987,1169,726,774,822,939,1185,
            1197,1198,1199,545,545
        };
    };
    public final static char baseAction[] = BaseAction.baseAction;
    public final int baseAction(int index) { return baseAction[index]; }
    public final static char lhs[] = baseAction;
    public final int lhs(int index) { return lhs[index]; };

    public interface TermCheck {
        public final static byte termCheck[] = {0,
            0,1,2,3,4,5,6,7,8,9,
            10,11,12,13,14,15,16,17,18,19,
            20,21,22,23,24,25,26,27,28,29,
            30,31,32,33,34,35,36,37,38,39,
            40,41,42,43,44,45,46,47,48,49,
            50,51,52,53,54,55,56,57,58,59,
            60,61,62,63,64,65,66,67,68,69,
            70,71,72,73,74,75,76,77,78,79,
            80,81,82,83,84,85,86,87,88,89,
            90,91,92,93,94,0,96,97,98,99,
            100,101,0,1,2,3,4,5,6,7,
            8,9,10,11,12,13,14,15,16,17,
            18,19,20,21,22,23,24,25,26,27,
            28,29,30,31,32,33,34,35,36,37,
            38,39,40,41,42,43,44,45,46,47,
            48,49,50,51,52,53,54,55,56,57,
            58,59,60,61,62,63,64,65,66,67,
            68,69,70,71,72,73,74,75,76,77,
            78,79,80,81,82,83,84,85,86,87,
            88,89,90,91,92,93,94,0,96,97,
            98,99,100,101,0,1,2,3,4,5,
            6,7,8,9,10,11,12,13,14,15,
            16,17,18,19,20,21,22,23,24,25,
            26,27,28,29,30,31,32,33,34,35,
            36,37,38,39,40,41,42,43,44,45,
            46,47,48,49,50,51,52,53,54,55,
            56,57,58,59,60,61,62,63,64,65,
            66,67,68,69,70,71,72,73,74,75,
            76,77,78,79,80,81,82,83,84,85,
            86,87,88,89,90,91,92,93,94,0,
            96,97,98,99,0,0,102,0,1,2,
            3,4,5,6,7,8,9,10,11,12,
            13,14,15,16,17,18,19,20,21,22,
            23,24,25,26,27,28,29,30,31,32,
            33,34,35,36,37,38,39,40,41,42,
            43,44,45,46,47,48,49,50,51,52,
            53,54,55,56,57,58,59,60,61,62,
            63,64,65,66,67,68,69,70,71,72,
            73,74,0,76,77,78,79,80,81,82,
            83,84,85,86,87,88,89,90,91,92,
            93,94,0,96,97,0,24,100,101,0,
            1,2,3,4,5,6,7,8,9,10,
            11,12,13,14,15,16,17,18,19,20,
            21,22,23,24,25,26,27,28,29,30,
            31,32,33,34,35,36,37,38,39,40,
            41,42,43,44,45,46,47,48,49,50,
            51,52,53,54,55,56,57,58,59,60,
            61,62,63,64,65,66,67,68,69,70,
            71,72,73,74,75,76,77,78,79,80,
            81,82,83,84,85,86,87,88,89,90,
            91,92,93,94,0,0,0,98,99,0,
            1,2,3,4,5,6,7,8,9,10,
            11,12,13,14,15,16,17,18,19,20,
            21,22,23,27,25,26,27,28,29,30,
            31,32,33,34,35,36,37,38,39,40,
            41,42,43,44,45,46,47,48,49,50,
            51,52,53,54,55,56,57,58,59,60,
            61,62,63,64,65,66,67,68,69,70,
            71,72,73,74,75,76,77,78,79,80,
            81,82,83,84,85,86,87,88,89,90,
            91,92,93,94,0,0,0,98,99,0,
            1,2,3,4,5,6,7,8,9,10,
            11,12,13,14,15,16,17,0,19,20,
            21,22,23,0,25,26,0,28,29,30,
            31,32,33,0,0,18,37,38,39,40,
            41,42,43,44,45,46,47,48,49,50,
            51,52,53,54,55,56,57,58,59,60,
            61,62,63,64,65,66,67,68,69,70,
            0,0,73,0,1,2,3,4,5,6,
            7,8,9,10,11,12,13,14,15,16,
            17,74,19,20,21,22,23,0,1,2,
            3,4,5,6,7,8,9,10,11,12,
            13,14,15,16,17,0,19,20,21,22,
            23,0,1,2,3,4,5,6,7,8,
            9,10,11,12,13,14,15,16,17,24,
            19,20,21,22,23,0,1,2,3,4,
            5,6,7,8,9,10,11,12,13,14,
            15,16,17,0,19,20,21,22,23,0,
            1,2,3,4,5,6,7,8,9,10,
            11,12,13,14,15,16,17,24,19,20,
            21,22,23,0,1,2,3,4,5,6,
            7,8,9,10,11,12,13,14,15,16,
            17,0,19,20,21,22,23,0,1,2,
            3,4,5,6,7,8,9,10,11,12,
            13,14,15,16,17,24,19,20,21,22,
            23,0,1,2,3,4,5,6,7,8,
            9,10,11,12,13,14,15,16,17,0,
            19,20,21,22,23,0,1,2,3,4,
            5,6,7,8,9,10,11,12,13,14,
            15,16,17,0,19,20,21,22,23,0,
            1,2,3,4,5,6,7,8,9,10,
            11,12,13,14,15,16,17,0,19,20,
            21,22,23,0,1,2,3,4,5,6,
            7,8,9,10,11,12,13,14,0,16,
            17,0,1,2,3,4,5,6,7,8,
            27,0,11,0,0,0,15,0,0,0,
            0,0,24,0,71,24,0,0,0,28,
            29,30,31,0,0,34,0,1,2,3,
            4,5,6,7,8,0,0,11,0,96,
            97,15,18,100,101,0,11,12,13,14,
            24,16,17,0,28,29,30,31,0,0,
            34,25,26,25,26,0,75,0,95,0,
            1,2,3,4,5,6,7,8,9,10,
            0,1,2,3,4,5,6,7,8,9,
            10,0,1,2,3,4,5,6,7,8,
            95,75,78,79,35,36,95,27,0,1,
            2,3,4,5,6,7,8,9,10,0,
            1,2,3,4,5,6,7,8,9,10,
            0,95,0,0,0,27,0,1,2,3,
            4,5,6,7,8,9,10,0,1,2,
            3,4,5,6,7,8,9,10,0,1,
            2,3,4,5,6,7,8,9,10,0,
            1,2,3,4,5,6,7,8,9,10,
            0,1,2,3,4,5,6,7,8,9,
            10,0,1,2,3,4,5,6,7,8,
            9,10,0,1,2,3,4,5,6,7,
            8,0,0,0,0,0,0,0,0,0,
            0,0,0,11,12,13,14,11,12,13,
            14,18,18,18,0,18,25,26,18,18,
            18,0,0,32,33,0,0,0,0,36,
            0,0,35,0,0,0,0,0,24,18,
            18,0,0,18,0,0,0,0,0,0,
            24,24,24,0,0,0,0,0,0,0,
            0,0,0,0,0,0,72,0,0,0,
            77,76,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,95,0,0,0,
            0,0,0,95,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0
        };
    };
    public final static byte termCheck[] = TermCheck.termCheck;
    public final int termCheck(int index) { return termCheck[index]; }

    public interface TermAction {
        public final static char termAction[] = {0,
            545,615,615,615,615,615,615,615,615,615,
            615,615,615,615,615,615,615,615,615,615,
            615,615,615,615,615,615,615,615,615,615,
            615,615,615,615,615,615,615,615,615,615,
            615,615,615,615,615,615,615,615,615,615,
            615,615,615,615,615,615,615,615,615,615,
            615,615,615,615,615,615,615,615,615,615,
            615,615,615,615,615,615,615,615,614,415,
            615,615,615,615,615,615,615,615,615,615,
            615,615,615,615,615,76,615,615,615,615,
            615,615,545,613,613,613,613,613,613,613,
            613,613,613,613,613,613,613,613,613,613,
            613,613,613,613,613,613,613,613,613,613,
            613,613,613,613,613,613,613,613,613,613,
            613,613,613,613,613,613,613,613,613,613,
            613,613,613,613,613,613,613,613,613,613,
            613,613,613,613,613,613,613,613,613,613,
            613,613,613,613,613,613,613,613,613,613,
            553,618,613,613,613,613,613,613,613,613,
            613,613,613,613,613,613,613,71,613,613,
            613,613,613,613,9,620,620,620,620,620,
            620,620,620,620,620,620,620,620,620,620,
            620,620,620,620,620,620,620,620,620,620,
            620,620,620,620,620,620,620,620,620,620,
            620,620,620,620,620,620,620,620,620,620,
            620,620,620,620,620,620,620,620,620,620,
            620,620,620,620,620,620,620,620,620,620,
            620,620,620,620,620,620,620,620,620,620,
            620,620,620,620,620,620,620,620,620,620,
            620,620,620,620,620,620,620,620,620,545,
            620,620,620,620,545,545,620,545,431,533,
            533,533,533,533,533,533,533,533,532,532,
            532,532,532,532,532,456,532,532,532,532,
            532,394,532,532,389,532,532,532,532,532,
            532,368,518,473,532,532,532,532,532,532,
            532,532,532,532,532,532,532,532,532,532,
            532,532,532,532,532,532,532,532,532,532,
            532,532,532,532,532,532,532,532,534,439,
            532,434,545,424,572,524,461,449,369,580,
            568,454,564,565,577,578,575,576,579,563,
            560,561,545,534,534,545,548,534,534,545,
            622,622,622,622,622,622,622,622,622,622,
            622,622,622,622,622,622,622,622,622,622,
            622,622,622,622,622,622,622,622,622,622,
            622,622,622,547,622,622,622,622,622,622,
            622,622,622,622,622,622,622,622,622,622,
            622,622,622,622,622,622,622,622,622,622,
            622,622,622,622,622,622,622,622,622,622,
            622,622,622,622,514,622,622,622,622,622,
            622,622,622,622,622,622,622,622,622,622,
            622,622,622,622,545,545,350,622,622,545,
            530,530,530,530,530,530,530,530,530,530,
            530,530,530,530,530,530,530,530,530,530,
            530,530,530,598,530,530,530,530,530,530,
            530,530,530,530,530,530,530,530,530,530,
            530,530,530,530,530,530,530,530,530,530,
            530,530,530,530,530,530,530,530,530,530,
            530,530,530,530,530,530,530,530,530,530,
            530,530,530,530,418,530,530,530,530,530,
            530,530,530,530,530,530,530,530,530,530,
            530,530,530,530,545,545,545,530,530,1,
            744,744,744,744,744,744,744,744,744,744,
            743,743,743,743,743,743,743,25,743,743,
            743,743,743,545,743,743,545,743,743,743,
            743,743,743,545,545,591,743,743,743,743,
            743,743,743,743,743,743,743,743,743,743,
            743,743,743,743,743,743,743,743,743,743,
            743,743,743,743,743,743,743,743,743,743,
            545,545,743,545,476,476,476,476,476,476,
            476,476,476,476,476,476,476,476,476,476,
            476,597,476,476,476,476,476,545,480,480,
            480,480,480,480,480,480,480,480,480,480,
            480,480,480,480,480,160,480,480,480,480,
            480,55,626,626,626,626,626,626,626,626,
            626,626,626,626,626,626,626,626,626,182,
            626,626,626,626,626,545,488,488,488,488,
            488,488,488,488,488,488,488,488,488,488,
            488,488,488,161,488,488,488,488,488,545,
            492,492,492,492,492,492,492,492,492,492,
            492,492,492,492,492,492,492,182,492,492,
            492,492,492,545,496,496,496,496,496,496,
            496,496,496,496,496,496,496,496,496,496,
            496,162,496,496,496,496,496,545,500,500,
            500,500,500,500,500,500,500,500,500,500,
            500,500,500,500,500,182,500,500,500,500,
            500,545,884,884,884,884,884,884,884,884,
            884,884,884,884,884,884,884,884,884,545,
            884,884,884,884,884,545,504,504,504,504,
            504,504,504,504,504,504,504,504,504,504,
            504,504,504,10,504,504,504,504,504,545,
            877,877,877,877,877,877,877,877,877,877,
            877,877,877,877,877,877,877,545,877,877,
            877,877,877,54,624,624,624,624,624,624,
            624,624,624,624,605,612,612,605,163,426,
            426,545,535,536,537,538,539,540,541,542,
            508,545,889,545,545,545,886,545,545,545,
            545,197,182,545,741,892,545,545,545,888,
            890,887,465,545,14,891,545,878,878,878,
            878,878,878,878,878,7,78,889,4,741,
            741,886,590,741,741,545,602,609,609,602,
            892,426,426,545,888,890,887,484,545,545,
            891,54,54,601,601,545,893,545,4,545,
            470,470,470,470,470,470,470,470,470,470,
            28,408,408,408,408,408,408,408,408,408,
            408,182,516,516,516,516,516,516,516,516,
            544,893,619,397,512,510,1,531,83,520,
            520,520,520,520,520,520,520,520,520,82,
            624,624,624,624,624,624,624,624,624,624,
            545,4,545,545,545,897,545,522,522,522,
            522,522,522,522,522,522,522,545,528,528,
            528,528,528,528,528,528,528,528,85,624,
            624,624,624,624,624,624,624,624,624,84,
            624,624,624,624,624,624,624,624,624,624,
            87,624,624,624,624,624,624,624,624,624,
            624,86,624,624,624,624,624,624,624,624,
            624,624,183,729,729,729,729,729,729,729,
            729,78,62,12,24,26,63,11,195,545,
            29,22,21,603,610,610,603,604,611,611,
            604,588,592,584,164,587,54,54,585,594,
            593,17,13,445,445,41,165,166,167,582,
            545,545,581,545,545,545,545,545,182,583,
            589,545,545,595,545,545,545,545,545,545,
            182,182,182,545,545,545,545,545,545,545,
            545,545,545,545,545,545,596,545,545,545,
            896,526,545,545,545,545,545,545,545,545,
            545,545,545,545,545,545,4,545,545,545,
            545,545,545,10
        };
    };
    public final static char termAction[] = TermAction.termAction;
    public final int termAction(int index) { return termAction[index]; }
    public final int asb(int index) { return 0; }
    public final int asr(int index) { return 0; }
    public final int nasb(int index) { return 0; }
    public final int nasr(int index) { return 0; }
    public final int terminalIndex(int index) { return 0; }
    public final int nonterminalIndex(int index) { return 0; }
    public final int scopePrefix(int index) { return 0;}
    public final int scopeSuffix(int index) { return 0;}
    public final int scopeLhs(int index) { return 0;}
    public final int scopeLa(int index) { return 0;}
    public final int scopeStateSet(int index) { return 0;}
    public final int scopeRhs(int index) { return 0;}
    public final int scopeState(int index) { return 0;}
    public final int inSymb(int index) { return 0;}
    public final String name(int index) { return null; }
    public final int getErrorSymbol() { return 0; }
    public final int getScopeUbound() { return 0; }
    public final int getScopeSize() { return 0; }
    public final int getMaxNameLength() { return 0; }

    public final static int
           NUM_STATES        = 66,
           NT_OFFSET         = 102,
           LA_STATE_OFFSET   = 897,
           MAX_LA            = 1,
           NUM_RULES         = 352,
           NUM_NONTERMINALS  = 41,
           NUM_SYMBOLS       = 143,
           SEGMENT_SIZE      = 8192,
           START_STATE       = 353,
           IDENTIFIER_SYMBOL = 0,
           EOFT_SYMBOL       = 95,
           EOLT_SYMBOL       = 103,
           ACCEPT_ACTION     = 544,
           ERROR_ACTION      = 545;

    public final static boolean BACKTRACK = false;

    public final int getNumStates() { return NUM_STATES; }
    public final int getNtOffset() { return NT_OFFSET; }
    public final int getLaStateOffset() { return LA_STATE_OFFSET; }
    public final int getMaxLa() { return MAX_LA; }
    public final int getNumRules() { return NUM_RULES; }
    public final int getNumNonterminals() { return NUM_NONTERMINALS; }
    public final int getNumSymbols() { return NUM_SYMBOLS; }
    public final int getSegmentSize() { return SEGMENT_SIZE; }
    public final int getStartState() { return START_STATE; }
    public final int getStartSymbol() { return lhs[0]; }
    public final int getIdentifierSymbol() { return IDENTIFIER_SYMBOL; }
    public final int getEoftSymbol() { return EOFT_SYMBOL; }
    public final int getEoltSymbol() { return EOLT_SYMBOL; }
    public final int getAcceptAction() { return ACCEPT_ACTION; }
    public final int getErrorAction() { return ERROR_ACTION; }
    public final boolean isValidForParser() { return isValidForParser; }
    public final boolean getBacktrack() { return BACKTRACK; }

    public final int originalState(int state) { return 0; }
    public final int asi(int state) { return 0; }
    public final int nasi(int state) { return 0; }
    public final int inSymbol(int state) { return 0; }

    public final int ntAction(int state, int sym) {
        return baseAction[state + sym];
    }

    public final int tAction(int state, int sym) {
        int i = baseAction[state],
            k = i + sym;
        return termAction[termCheck[k] == sym ? k : i];
    }
    public final int lookAhead(int la_state, int sym) {
        int k = la_state + sym;
        return termAction[termCheck[k] == sym ? k : la_state];
    }
}
/**
 * 定数定義
 */
var FIELD_X_MAX = 6;
var FIELD_Y_MAX = 12;
var UNIT_SIZE = 32;
var PUYO_GIF = 'http://www.puyop.com/img/sim/puyo.png';

var NEXT_SETTING_COL = 5;
var NEXT_SETTING_ROW = 20;
var NEXT_SETTING_NUM = 100;

var NEXT_MAX = 2;	// 表示するネクストの最大数
var HAND_PUYO_NUM_MAX = 4;	// 1手に含まれるぷよの最大数

var COLOR_HISTORY_MAX = 128;	// 出現色リストの配列数

// ぷよタイプ
var TYPE_NONE     = 0;
var TYPE_AKA      = 1;
var TYPE_MIDORI   = 2;
var TYPE_AO       = 3;
var TYPE_KI       = 4;
var TYPE_MURASAKI = 5;
var TYPE_OJAMA    = 6;
var TYPE_KATA     = 7;
var TYPE_TETSU    = 8;
var TYPE_KABE     = 9;
var TYPE_KESU     = 10;

var TYPE_BEGIN = TYPE_AKA;
var TYPE_END   = TYPE_KESU;
var TYPE_PUYO_BEGIN = TYPE_AKA;
var TYPE_PUYO_END   = TYPE_MURASAKI;
var TYPE_OJAMA_BEGIN = TYPE_OJAMA;
var TYPE_OJAMA_END   = TYPE_KATA;
var TYPE_COND_PUYO_BEGIN = TYPE_NONE;
var TYPE_COND_PUYO_END   = TYPE_KATA;
var TYPE_LENGTHY_FCODE = TYPE_KATA;	// 冗長URLの対象タイプの開始

// 手タイプ
var HAND_TYPE_NONE =  0;	// なし
var HAND_TYPE_2    = 20;	// 2個ぷよ
var HAND_TYPE_3A   = 31;	// 3個ぷよ縦型
var HAND_TYPE_3B   = 32;	// 3個ぷよ横型
var HAND_TYPE_4A   = 41;	// 4個ぷよ2色
var HAND_TYPE_4B   = 42;	// でかぷよ
var HAND_TYPE_OJ   = 99;	// おじゃまぷよ

var OJAMA_ROW_MAX = 5;	// おじゃまぷよ段数の最大値

// フィールドコード
var FCODE_CHAR_BIT = 6;	// 1文字で表せられるビット数
var FCODE_NEXT_DATA_BIT = 12;	// ネクスト設定の1手及びぷよ譜のデータを表すビット数
var FCODE_NAZO_COND_BIT      = 4;	// なぞぷよクリア条件のデータを表すビット数
var FCODE_NAZO_COND_PUYO_BIT = 3;	// なぞぷよクリア条件の【ぷよ】のデータを表すビット数
var FCODE_NAZO_COND_NUM_BIT  = 7;	// なぞぷよクリア条件の【n】のデータを表すビット数



var ENCODE_CHAR = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ[]';

var MODE_EDITOR = 1;	// エディタ
var MODE_TOKOPUYO = 2;	// とこぷよ

var TOKO_MODE_NORMAL = 1;	// 普通にとこぷよ
var TOKO_MODE_EDIT = 2;		// ぷよ譜／なぞぷよ

var HAND_START_X = 3;
var HAND_START_Y = 1;



// 連鎖ボーナス
var RENSA_BONUS_LIST = [
	[0, 0, 8, 16, 32, 64, 96, 128, 160, 192, 224, 256, 288, 320, 352, 384, 416, 448, 480, 512]	// ぷよぷよ通
];
// 多色ボーナス
var COLOR_BONUS_LIST = [
	[0, 0, 3, 6, 12, 24],
	[0, 0, 2, 4, 8, 16]
];
// 連結ボーナス
var COMBI_BONUS_LIST = [
	[0, 0, 0, 0, 0, 2, 3, 4, 5, 6, 7, 10],
	[0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 8]
];

// ネクストの出現パターン
var PATTERN_LOOP_NUM = 16;
var PATTERN_LIST = [
	[20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20]	// ぷよぷよ通
];

var FEVER_CHARA_LIST = {
	'20th': [
		{name:'アルル',
		 pattern:[20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20],
		 normal:[0, 0, 8, 17, 23, 35, 71, 118, 178, 239, 300, 377, 454, 534, 613, 693, 699, 699, 699, 699],
		 fever:[0, 0, 6, 11, 14, 19, 30, 50, 76, 101, 151, 176, 181, 216, 252, 277, 302, 328, 353, 378]},
		{name:'アミティ',
		 pattern:[20, 20, 20, 31, 20, 20, 20, 42, 20, 20, 20, 32, 20, 20, 20, 41],
		 normal:[0, 0, 8, 17, 22, 34, 67, 112, 168, 224, 280, 350, 420, 490, 560, 630, 699, 699, 699, 699],
		 fever:[0, 0, 7, 13, 15, 21, 34, 56, 84, 112, 168, 196, 202, 239, 280, 308, 336, 364, 392, 420]},
		{name:'りんご',
		 pattern:[20, 20, 20, 31, 42, 20, 20, 20, 32, 42, 41, 20, 20, 31, 20, 31],
		 normal:[0, 0, 8, 15, 20, 30, 60, 101, 151, 202, 252, 315, 378, 441, 504, 567, 630, 693, 699, 699],
		 fever:[0, 0, 6, 12, 15, 22, 35, 59, 91, 122, 186, 220, 228, 276, 328, 365, 403, 437, 470, 504]},
		{name:'シグ',
		 pattern:[20, 20, 20, 31, 20, 20, 20, 41, 20, 20, 32, 20, 42, 20, 20, 41],
		 normal:[0, 0, 8, 15, 20, 30, 60, 101, 151, 202, 252, 315, 378, 441, 504, 567, 630, 693, 699, 699],
		 fever:[0, 0, 8, 14, 17, 24, 38, 64, 97, 130, 197, 230, 237, 283, 333, 368, 403, 437, 470, 504]},
		{name:'ラフィーナ',
		 pattern:[20, 20, 31, 20, 20, 42, 20, 20, 20, 32, 20, 20, 31, 20, 20, 41],
		 normal:[0, 0, 8, 17, 23, 36, 74, 125, 192, 260, 330, 420, 512, 617, 699, 699, 699, 699, 699, 699],
		 fever:[0, 0, 6, 10, 14, 20, 32, 54, 83, 111, 169, 201, 209, 252, 300, 334, 370, 400, 431, 462]},
		{name:'シェゾ',
		 pattern:[20, 20, 20, 20, 31, 20, 41, 20, 32, 20, 42, 20, 31, 20, 41, 20],
		 normal:[0, 0, 8, 16, 23, 36, 77, 132, 202, 274, 350, 447, 546, 661, 699, 699, 699, 699, 699, 699],
		 fever:[0, 0, 6, 10, 13, 17, 29, 48, 72, 97, 146, 171, 177, 211, 249, 276, 302, 328, 353, 378]},
		{name:'ルルー',
		 pattern:[20, 20, 20, 41, 20, 20, 20, 42, 20, 20, 20, 41, 20, 20, 20, 42],
		 normal:[0, 0, 8, 17, 23, 36, 74, 125, 192, 260, 330, 420, 512, 617, 699, 699, 699, 699, 699, 699],
		 fever:[0, 0, 6, 10, 14, 20, 32, 54, 78, 106, 160, 190, 198, 239, 284, 317, 351, 380, 409, 439]},
		{name:'サタン',
		 pattern:[20, 20, 20, 31, 20, 20, 20, 42, 20, 20, 20, 41, 20, 20, 20, 42],
		 normal:[0, 0, 8, 16, 23, 36, 71, 117, 175, 232, 288, 359, 428, 536, 676, 699, 699, 699, 699, 699],
		 fever:[0, 0, 6, 10, 13, 17, 29, 48, 72, 97, 146, 171, 177, 211, 249, 276, 302, 328, 353, 378]},
		{name:'カーバンクル',
		 pattern:[20, 31, 42, 20, 41, 32, 20, 42, 20, 31, 41, 20, 42, 32, 20, 41],
		 normal:[0, 0, 8, 17, 22, 34, 67, 112, 168, 224, 280, 350, 420, 490, 560, 630, 699, 699, 699, 699],
		 fever:[0, 0, 7, 13, 15, 21, 34, 56, 84, 112, 168, 196, 202, 239, 280, 308, 336, 364, 392, 420]},
		{name:'すけとうだら',
		 pattern:[20, 20, 31, 20, 20, 32, 20, 42, 20, 31, 20, 20, 32, 20, 20, 42],
		 normal:[0, 0, 8, 15, 20, 29, 57, 94, 141, 187, 232, 289, 344, 397, 451, 504, 556, 608, 655, 699],
		 fever:[0, 0, 8, 14, 17, 24, 38, 64, 97, 130, 197, 230, 237, 283, 333, 368, 403, 437, 470, 504]},
		{name:'ウィッチ',
		 pattern:[32, 31, 20, 20, 32, 20, 20, 20, 41, 20, 20, 20, 42, 20, 20, 20],
		 normal:[0, 0, 8, 16, 21, 32, 64, 106, 160, 213, 266, 333, 399, 465, 532, 598, 628, 654, 678, 699],
		 fever:[0, 0, 7, 13, 16, 22, 34, 57, 87, 115, 174, 204, 209, 250, 293, 323, 353, 382, 412, 441]},
		{name:'ドラコケンタウロス',
		 pattern:[20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 31, 32, 42, 32, 31, 41],
		 normal:[0, 0, 8, 17, 24, 36, 74, 125, 188, 253, 319, 402, 487, 578, 666, 699, 699, 699, 699, 699],
		 fever:[0, 0, 6, 10, 13, 17, 27, 45, 67, 90, 134, 157, 161, 192, 224, 246, 269, 291, 314, 336]},
		{name:'リデル',
		 pattern:[20, 20, 20, 31, 20, 20, 42, 20, 20, 32, 20, 20, 42, 20, 20, 41],
		 normal:[0, 0, 8, 17, 24, 36, 74, 125, 188, 253, 319, 402, 487, 578, 666, 699, 699, 699, 699, 699],
		 fever:[0, 0, 6, 10, 13, 17, 27, 45, 67, 90, 134, 157, 161, 192, 224, 246, 269, 291, 314, 336]},
		{name:'クルーク',
		 pattern:[20, 20, 31, 20, 32, 20, 42, 20, 31, 20, 42, 20, 32, 20, 20, 41],
		 normal:[0, 0, 8, 16, 23, 36, 77, 132, 202, 274, 350, 447, 546, 661, 699, 699, 699, 699, 699, 699],
		 fever:[0, 0, 6, 11, 14, 19, 30, 50, 76, 101, 151, 176, 181, 216, 252, 277, 302, 328, 353, 378]},
		{name:'フェーリ',
		 pattern:[31, 20, 42, 20, 32, 20, 41, 20, 42, 20, 31, 20, 41, 20, 32, 20],
		 normal:[0, 0, 8, 15, 20, 29, 57, 94, 141, 187, 232, 289, 344, 397, 451, 504, 556, 608, 655, 699],
		 fever:[0, 0, 8, 13, 17, 22, 35, 59, 87, 116, 173, 201, 206, 243, 283, 309, 336, 364, 392, 420]},
		{name:'レムレス',
		 pattern:[20, 31, 20, 42, 20, 32, 20, 20, 20, 31, 20, 41, 20, 42, 20, 32],
		 normal:[0, 0, 8, 17, 22, 34, 67, 112, 168, 224, 280, 350, 420, 490, 560, 630, 699, 699, 699, 699],
		 fever:[0, 0, 7, 13, 15, 20, 32, 53, 79, 105, 156, 181, 186, 219, 255, 279, 302, 328, 353, 378]},
		{name:'アコール先生',
		 pattern:[20, 31, 20, 32, 20, 31, 20, 42, 20, 32, 20, 31, 20, 32, 20, 41],
		 normal:[0, 0, 8, 17, 23, 36, 74, 125, 192, 260, 330, 420, 512, 617, 699, 699, 699, 699, 699, 699],
		 fever:[0, 0, 6, 11, 14, 19, 30, 50, 76, 101, 151, 176, 181, 216, 252, 277, 302, 328, 353, 378]},
		{name:'ユウちゃん＆レイくん',
		 pattern:[20, 31, 20, 42, 20, 32, 20, 42, 20, 31, 20, 41, 20, 20, 20, 41],
		 normal:[0, 0, 8, 15, 20, 30, 60, 101, 151, 202, 252, 315, 378, 441, 504, 567, 630, 693, 699, 699],
		 fever:[0, 0, 6, 12, 15, 22, 35, 59, 91, 122, 186, 220, 228, 276, 328, 365, 403, 437, 470, 504]},
		{name:'おにおん',
		 pattern:[20, 20, 20, 20, 20, 42, 20, 20, 20, 20, 20, 31, 20, 20, 20, 41],
		 normal:[0, 0, 8, 15, 21, 31, 64, 107, 161, 216, 272, 342, 412, 485, 557, 630, 699, 699, 699, 699],
		 fever:[0, 0, 8, 15, 17, 24, 37, 61, 91, 120, 178, 206, 211, 247, 286, 311, 336, 364, 392, 420]},
		{name:'どんぐりガエル',
		 pattern:[20, 20, 20, 31, 20, 20, 20, 41, 20, 20, 20, 42, 20, 20, 20, 41],
		 normal:[0, 0, 9, 17, 23, 34, 67, 111, 164, 217, 269, 332, 395, 451, 510, 567, 623, 678, 699, 699],
		 fever:[0, 0, 7, 13, 15, 20, 32, 53, 79, 105, 156, 181, 186, 219, 255, 279, 302, 328, 353, 378]},
		{name:'さかな王子',
		 pattern:[20, 20, 20, 31, 20, 20, 32, 42, 20, 20, 31, 42, 20, 20, 32, 41],
		 normal:[0, 0, 8, 17, 23, 36, 74, 125, 192, 260, 330, 420, 512, 617, 699, 699, 699, 699, 699, 699],
		 fever:[0, 0, 6, 11, 14, 19, 30, 50, 76, 101, 151, 176, 181, 216, 252, 277, 302, 328, 353, 378]},
		{name:'まぐろ',
		 pattern:[20, 20, 31, 20, 20, 32, 20, 20, 41, 20, 20, 42, 20, 20, 20, 41],
		 normal:[0, 0, 9, 17, 23, 34, 67, 111, 164, 217, 269, 332, 395, 451, 510, 567, 623, 678, 699, 699],
		 fever:[0, 0, 7, 13, 15, 20, 32, 53, 79, 105, 156, 181, 186, 219, 255, 279, 302, 328, 353, 378]},
		{name:'りすくませんぱい',
		 pattern:[20, 32, 20, 20, 20, 20, 42, 20, 20, 20, 20, 41, 20, 20, 31, 20],
		 normal:[0, 0, 8, 15, 21, 31, 64, 107, 161, 216, 272, 342, 412, 485, 557, 630, 699, 699, 699, 699],
		 fever:[0, 0, 8, 15, 17, 24, 37, 61, 91, 120, 178, 206, 211, 247, 286, 311, 336, 364, 392, 420]},
		{name:'エコロ',
		 pattern:[20, 31, 32, 31, 20, 20, 31, 32, 31, 20, 41, 42, 20, 20, 31, 20],
		 normal:[0, 0, 8, 17, 23, 36, 74, 125, 192, 260, 330, 420, 512, 617, 699, 699, 699, 699, 699, 699],
		 fever:[0, 0, 6, 11, 14, 19, 30, 50, 76, 101, 151, 176, 181, 216, 252, 277, 302, 328, 353, 378]}
	],
	'win': [
		{name:'アミティ',
		 pattern:[20, 20, 20, 31, 20, 20, 20, 42, 20, 20, 20, 32, 20, 20, 20, 41],
		 normal:[0, 0, 8, 16, 22, 33, 67, 112, 168, 224, 280, 350, 420, 490, 560, 630, 699, 699, 699, 699],
		 fever:[0, 0, 7, 12, 15, 21, 33, 56, 84, 112, 168, 196, 201, 239, 280, 308, 336, 364, 392, 420]},
		{name:'ラフィーナ',
		 pattern:[20, 20, 31, 20, 20, 42, 20, 20, 20, 32, 20, 20, 32, 20, 20, 41],
		 normal:[0, 0, 7, 16, 23, 35, 74, 125, 191, 259, 330, 420, 512, 617, 699, 699, 699, 699, 699, 699],
		 fever:[0, 0, 6, 11, 14, 19, 32, 53, 80, 107, 163, 191, 197, 235, 277, 306, 336, 364, 392, 420]},
		{name:'アコール先生',
		 pattern:[20, 31, 20, 31, 20, 31, 20, 42, 20, 32, 20, 32, 20, 32, 20, 41],
		 normal:[0, 0, 7, 16, 23, 35, 74, 125, 191, 259, 330, 420, 512, 617, 699, 699, 699, 699, 699, 699],
		 fever:[0, 0, 7, 12, 14, 20, 32, 53, 79, 105, 156, 181, 186, 219, 254, 278, 302, 327, 352, 378]},
		{name:'クルーク',
		 pattern:[20, 20, 31, 20, 31, 20, 42, 20, 32, 20, 42, 20, 31, 20, 20, 41],
		 normal:[0, 0, 7, 16, 23, 37, 77, 131, 201, 274, 350, 446, 546, 661, 699, 699, 699, 699, 699, 699],
		 fever:[0, 0, 6, 11, 14, 18, 30, 50, 75, 100, 151, 176, 181, 215, 252, 277, 302, 327, 352, 378]},
		{name:'リデル',
		 pattern:[20, 20, 20, 31, 20, 20, 42, 20, 20, 32, 20, 20, 42, 20, 20, 41],
		 normal:[0, 0, 9, 18, 24, 37, 74, 123, 184, 246, 308, 385, 462, 539, 616, 693, 699, 699, 699, 699],
		 fever:[0, 0, 5, 9, 12, 16, 26, 44, 67, 89, 134, 156, 161, 191, 224, 246, 268, 291, 313, 336]},
		{name:'タルタル',
		 pattern:[20, 20, 31, 20, 20, 42, 20, 32, 20, 20, 42, 20, 20, 31, 20, 41],
		 normal:[0, 0, 9, 17, 23, 34, 67, 110, 164, 217, 268, 332, 394, 450, 509, 567, 623, 677, 699, 699],
		 fever:[0, 0, 7, 12, 14, 20, 32, 53, 79, 105, 156, 181, 186, 219, 254, 278, 302, 327, 352, 378]},
		{name:'おしゃれコウベ',
		 pattern:[20, 20, 20, 20, 31, 20, 20, 20, 20, 42, 20, 20, 20, 20, 20, 41],
		 normal:[0, 0, 7, 15, 21, 31, 63, 107, 161, 216, 271, 341, 411, 485, 557, 630, 699, 699, 699, 699],
		 fever:[0, 0, 7, 14, 17, 23, 38, 64, 97, 130, 196, 230, 237, 283, 333, 368, 403, 436, 470, 504]},
		{name:'どんぐりガエル',
		 pattern:[20, 20, 20, 31, 20, 20, 20, 41, 20, 20, 20, 42, 20, 20, 20, 41],
		 normal:[0, 0, 9, 17, 23, 34, 67, 110, 164, 217, 268, 332, 394, 450, 509, 567, 623, 677, 699, 699],
		 fever:[0, 0, 7, 12, 14, 20, 32, 53, 79, 105, 156, 181, 186, 219, 254, 278, 302, 327, 352, 378]},
		{name:'こづれフランケン',
		 pattern:[20, 20, 31, 20, 20, 31, 20, 42, 20, 32, 20, 32, 20, 32, 20, 41],
		 normal:[0, 0, 9, 17, 22, 32, 63, 105, 154, 203, 249, 306, 361, 406, 456, 504, 549, 592, 634, 673],
		 fever:[0, 0, 7, 13, 15, 20, 32, 52, 77, 101, 149, 171, 175, 203, 232, 251, 268, 291, 313, 336]},
		{name:'おにおん',
		 pattern:[20, 20, 20, 20, 20, 42, 20, 20, 20, 20, 20, 32, 20, 20, 20, 41],
		 normal:[0, 0, 7, 15, 21, 31, 63, 107, 161, 216, 271, 341, 411, 485, 557, 630, 699, 699, 699, 699],
		 fever:[0, 0, 8, 14, 17, 23, 37, 60, 91, 119, 177, 205, 210, 247, 285, 310, 336, 364, 392, 420]},
		{name:'さかな王子',
		 pattern:[20, 20, 20, 31, 20, 20, 31, 42, 20, 20, 32, 42, 20, 20, 31, 41],
		 normal:[0, 0, 7, 15, 21, 31, 63, 107, 161, 216, 271, 341, 411, 485, 557, 630, 699, 699, 699, 699],
		 fever:[0, 0, 7, 13, 16, 23, 38, 65, 99, 133, 203, 240, 248, 299, 355, 395, 436, 473, 509, 546]},
		{name:'ユウちゃん',
		 pattern:[20, 31, 20, 42, 20, 32, 20, 42, 20, 31, 20, 41, 20, 20, 20, 41],
		 normal:[0, 0, 7, 16, 21, 32, 67, 113, 171, 231, 291, 367, 445, 529, 610, 692, 699, 699, 699, 699],
		 fever:[0, 0, 7, 13, 16, 22, 37, 62, 93, 126, 191, 225, 233, 280, 330, 366, 403, 436, 470, 504]},
		{name:'ほほうどり',
		 pattern:[20, 20, 20, 31, 20, 20, 20, 31, 20, 20, 20, 31, 20, 20, 20, 41],
		 normal:[0, 0, 7, 15, 20, 30, 60, 100, 151, 201, 252, 315, 378, 441, 504, 567, 630, 693, 699, 699],
		 fever:[0, 0, 8, 15, 18, 25, 40, 67, 100, 134, 201, 235, 242, 287, 336, 369, 403, 436, 470, 504]},
		{name:'アルル',
		 pattern:[20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20],
		 normal:[0, 0, 8, 16, 23, 34, 70, 118, 177, 238, 299, 376, 453, 534, 613, 692, 699, 699, 699, 699],
		 fever:[0, 0, 7, 12, 14, 20, 32, 53, 79, 105, 156, 181, 186, 219, 254, 278, 302, 327, 352, 378]},
		{name:'ポポイ',
		 pattern:[20, 20, 31, 20, 20, 31, 20, 20, 31, 20, 20, 42, 20, 42, 20, 41],
		 normal:[0, 0, 7, 15, 20, 30, 60, 100, 151, 201, 252, 315, 378, 441, 504, 567, 630, 693, 699, 699],
		 fever:[0, 0, 8, 15, 18, 25, 40, 67, 100, 134, 201, 235, 242, 287, 336, 369, 403, 436, 470, 504]},
		{name:'カーバンクル',
		 pattern:[20, 31, 42, 20, 41, 32, 20, 42, 20, 31, 41, 20, 42, 32, 20, 41],
		 normal:[0, 0, 8, 16, 23, 34, 70, 118, 177, 238, 299, 376, 453, 534, 613, 692, 699, 699, 699, 699],
		 fever:[0, 0, 6, 11, 14, 19, 32, 53, 80, 107, 163, 191, 197, 235, 277, 306, 336, 364, 392, 420]}
	]
};



// フィーバーの種
var FEVER_TANE = [
	// 階段
	{
		 3: 'po0z80z80z8',
		 4: 'x01qo4bg4bg4bg',
		 5: 'q82cg3hw3hyjhy',
		 6: '14k2pA4ja4ja4ja',
		 7: 'o00w00o00s00qc8zhxzhxzhx',
		 8: 'o00g00o00q4csqAjzhjzhjzh',
		 9: '800o00z00s00sxhqc9zhyzhyzhy',
		10: 'o00w00s00s00ykwshpqc9zhzzhzzhz',
		11: 'o00w00sg0sg3yAzshpqc9zhzzhzzhz',
		12: 'o03w02pg39giqrqpkkqbAzhqzhqzhq',
		13: '2o04w0bsg9sgryAzshpqc9zhzzhzzhz',
		14: '4001o04g1sqxbszsi9cqzjspqjzajzajzb',
		15: 'gg0r81ppihkbqkahxrpkayhziqcsqcsqcs'
	},
	// 挟み込み
	{
		 3: '400300q00r04y04qg',
		 4: 'w00q80r84yg4q9',
		 5: '10010040qc0r94yk4qc',
		 6: '30020041jc1i9Absxjc',
		 7: 'w00o00o03o03w04xjcxi9rbspjc',
		 8: 'w00g00A00kp4jhsjirApczhs',
		 9: 'A00c00zq0cs2acqa9rAkyycq',
		10: 'Ao0co1zi1cqabhqbirApazhq',
		11: 'Aq0ki3zpbkqajhyjiAApazhy',
		12: '3Aq1kirzpbkqajhyjiAApazhy',
		13: '300100z9qAhirbpbhqajcqj9r9sybcq',
		14: '100b00pg0rAa9harAhbkaajhyjiAApazhy',
		15: '900k0rqq9iAq9kiAjpcAqajhqjirApazhq'
	},
	// 平積み
	{
		 3: '100101i0r90pi',
		 4: '1piAz9rpi',
		 5: 'g00w00w01w01ipikz9zpi',
		 6: 'w00b00rw09qAczizqA',
		 7: '800j00sw0rz0ipihb9bpi',
		 8: 'k009g0ir09j8Apiyj9jpi',
		 9: 'c00c00iAoAr9iAp9picz9zpi',
		10: '2x04x04rbi9syrbsApixb9bpi',
		11: '1w028w3s8rpjiiparjp9picz9zpi',
		12: 'h00Ac0ic2AiziApaizp9picz9zpi',
		13: 'p03A29rabAAkrixbAkx9piaj9jpi',
		14: '4oc4xryr9ypr9j9isx9jsApiyj9jpi',
		15: 'w0pj0qx4x9srAzAisrrzAApcrzpipikz9zpi'
	},
	// 座布団
	{
		 3: 'o00800g00ro0iho9bo',
		 4: 'g00800o00w00ro0Aw0rpg9ag',
		 5: 'o00800g00w00o00980ro0Aw0iho9bo',
		 6: 'w00o00800w00g00800ro0980ig0Aw09bwrsw',
		 7: '800g00g00w00800g00Aw0ig0980Awwiso9bo',
		 8: 'w00o00o00w00g00800ro0980ig2AwqrjjAyj',
		 9: '800g00g00w00800g00Aw3ig198rAzbipp9bp',
		10: '800g00g00w03g01803ro498AikrAzbipp9bp',
		11: '800g00g03801o0kg0jAwrijirqA9ccipp9bp',
		12: '800g00g00w00ow48rkAzy9cirqrAzbipp9bp',
		13: '800g00g00w02sr1jihAzaihirqrAzbipp9bp',
		14: '800g00g4bw4xc3cjpcAzyiki9arAzbipp9bp',
		15: 'o0i80rh0Aj4rA3kjqkAzxik9rprAzbipp9bp'
	}
];



// なぞぷよクリア条件
var NAZO_COND_NONE             =  0;	// 設定なし
var NAZO_COND_P_ALL            =  1;	// 【ぷよ】全て消す
var NAZO_COND_N_EQ_COLOR       =  2;	// 【n】色消す
var NAZO_COND_N_GE_COLOR       =  3;	// 【n】色以上消す
var NAZO_COND_P_N_EQ_NUM       =  4;	// 【ぷよ】【n】個消す
var NAZO_COND_P_N_GE_NUM       =  5;	// 【ぷよ】【n】個以上消す
var NAZO_COND_N_EQ_RENSA       =  6;	// 【n】連鎖する
var NAZO_COND_N_GE_RENSA       =  7;	// 【n】連鎖以上する
var NAZO_COND_N_EQ_RENSA_P_ALL =  8;	// 【n】連鎖＆【ぷよ】全て消す
var NAZO_COND_N_GE_RENSA_P_ALL =  9;	// 【n】連鎖以上＆【ぷよ】全て消す
var NAZO_COND_SAME_N_EQ_COLOR  = 10;	// 【n】色同時に消す
var NAZO_COND_SAME_N_GE_COLOR  = 11;	// 【n】色以上同時に消す
var NAZO_COND_SAME_P_N_EQ_NUM  = 12;	// 【ぷよ】【n】個同時に消す
var NAZO_COND_SAME_P_N_GE_NUM  = 13;	// 【ぷよ】【n】個以上同時に消す
var NAZO_COND_SAME_P_N_EQ_POS  = 14;	// 【ぷよ】【n】組同時に消す
var NAZO_COND_SAME_P_N_GE_POS  = 15;	// 【ぷよ】【n】組以上同時に消す

var NAZO_COND_LIST = [
	{text:'設定なし',							puyo:false,	num:false},
	{text:'【ぷよ】全て消す',					puyo:true,	num:false},
	{text:'【n】色消す',						puyo:false,	num:true},
	{text:'【n】色以上消す',					puyo:false,	num:true},
	{text:'【ぷよ】【n】個消す',				puyo:true,	num:true},
	{text:'【ぷよ】【n】個以上消す',			puyo:true,	num:true},
	{text:'【n】連鎖する',						puyo:false,	num:true},
	{text:'【n】連鎖以上する',					puyo:false,	num:true},
	{text:'【n】連鎖＆【ぷよ】全て消す',		puyo:true,	num:true},
	{text:'【n】連鎖以上＆【ぷよ】全て消す',	puyo:true,	num:true},
	{text:'【n】色同時に消す',					puyo:false,	num:true},
	{text:'【n】色以上同時に消す',				puyo:false,	num:true},
	{text:'【ぷよ】【n】個同時に消す',			puyo:true,	num:true},
	{text:'【ぷよ】【n】個以上同時に消す',		puyo:true,	num:true},
	{text:'【ぷよ】【n】組同時に消す',			puyo:true,	num:true},
	{text:'【ぷよ】【n】組以上同時に消す',		puyo:true,	num:true}
];

var NAZO_COND_PUYO_TYPE_LIST = [
	[TYPE_AKA],			// 赤ぷよ
	[TYPE_MIDORI],		// 緑ぷよ
	[TYPE_AO],			// 青ぷよ
	[TYPE_KI],			// 黄ぷよ
	[TYPE_MURASAKI],	// 紫ぷよ
	[TYPE_AKA, TYPE_MIDORI, TYPE_AO, TYPE_KI, TYPE_MURASAKI],	// 色ぷよ
	[TYPE_OJAMA, TYPE_KATA],	// おじゃまぷよ
	[TYPE_AKA, TYPE_MIDORI, TYPE_AO, TYPE_KI, TYPE_MURASAKI, TYPE_OJAMA, TYPE_KATA]	// 鉄壁以外のぷよ
];



// キー定義
var KEY_LEFT	= 37;
var KEY_UP		= 38;
var KEY_RIGHT	= 39;
var KEY_DOWN	= 40;
var KEY_0		= 48;
var KEY_1		= 49;
var KEY_2		= 50;
var KEY_3		= 51;
var KEY_4		= 52;
var KEY_5		= 53;
var KEY_6		= 54;
var KEY_7		= 55;
var KEY_8		= 56;
var KEY_9		= 57;
var KEY_A		= 65;
var KEY_B		= 66;
var KEY_C		= 67;
var KEY_D		= 68;
var KEY_E		= 69;
var KEY_F		= 70;
var KEY_G		= 71;
var KEY_H		= 72;
var KEY_I		= 73;
var KEY_J		= 74;
var KEY_K		= 75;
var KEY_L		= 76;
var KEY_M		= 77;
var KEY_N		= 78;
var KEY_O		= 79;
var KEY_P		= 80;
var KEY_Q		= 81;
var KEY_R		= 82;
var KEY_S		= 83;
var KEY_T		= 84;
var KEY_U		= 85;
var KEY_V		= 86;
var KEY_W		= 87;
var KEY_X		= 88;
var KEY_Y		= 89;
var KEY_Z		= 90;

var KEY = new Array();

KEY[8]  = "BackSpace";
KEY[9]  = "Tab";
KEY[13] = "Enter";
KEY[16] = "Shift";
KEY[17] = "Ctrl";
KEY[18] = "Alt";
KEY[19] = "PauseBreak";
KEY[27] = "Esc";
KEY[28] = "変換";
KEY[29] = "無変換";
KEY[32] = "Space";
KEY[37] = "←";
KEY[38] = "↑";
KEY[39] = "→";
KEY[40] = "↓";
KEY[45] = "Insert";
KEY[46] = "Delete";
KEY[48] = "0";
KEY[49] = "1";
KEY[50] = "2";
KEY[51] = "3";
KEY[52] = "4";
KEY[53] = "5";
KEY[54] = "6";
KEY[55] = "7";
KEY[56] = "8";
KEY[57] = "9";
KEY[59] = ":";		//FireFox
KEY[65] = "A";
KEY[66] = "B";
KEY[67] = "C";
KEY[68] = "D";
KEY[69] = "E";
KEY[70] = "F";
KEY[71] = "G";
KEY[72] = "H";
KEY[73] = "I";
KEY[74] = "J";
KEY[75] = "K";
KEY[76] = "L";
KEY[77] = "M";
KEY[78] = "N";
KEY[79] = "O";
KEY[80] = "P";
KEY[81] = "Q";
KEY[82] = "R";
KEY[83] = "S";
KEY[84] = "T";
KEY[85] = "U";
KEY[86] = "V";
KEY[87] = "M";
KEY[88] = "X";
KEY[89] = "Y";
KEY[90] = "Z";
KEY[91] = "Windows";
KEY[107] = ";";		//FireFox
KEY[109] = "-";		//FireFox
KEY[112] = "F1";
KEY[113] = "F2";
KEY[114] = "F3";
KEY[115] = "F4";
KEY[116] = "F5";
KEY[117] = "F6";
KEY[118] = "F7";
KEY[119] = "F8";
KEY[120] = "F9";
KEY[121] = "F10";
KEY[122] = "F11";
KEY[123] = "F12";
KEY[186] = ":";		//IE
KEY[187] = ";";		//IE
KEY[188] = ",";
KEY[189] = "-";		//IE
KEY[190] = ".";
KEY[191] = "/";
KEY[192] = "@";
KEY[219] = "[";
KEY[220] = "\\";
KEY[221] = "]";
KEY[222] = "^";
KEY[226] = "\\";
KEY[240] = "CapsLock";
KEY[242] = "カナ/ひら";


/**
 * 関数定義 - データ
 */
var simData = {
	/* ============================================================ */
	_dispMode: MODE_EDITOR,	// 表示モード

	/**
	 * 表示モードを取得
	 */
	getDispMode: function(){
		return this._dispMode;
	},

	/**
	 * 表示モードをセット
	 */
	setDispMode: function(mode){
		this._dispMode = mode;
	},

	/* ============================================================ */
	_tokoMode: TOKO_MODE_NORMAL,	// とこぷよモード

	/**
	 * とこぷよモードを取得
	 */
	getTokoMode: function(){
		return this._tokoMode;
	},

	/**
	 * とこぷよモードをセット
	 */
	setTokoMode: function(mode){
		this._tokoMode = mode;
	},

	/* ============================================================ */
	_rensaFlag: false,		// 連鎖実行中フラグ

	/**
	 * 連鎖実行中フラグを取得
	 */
	getRensaFlag: function(){
		return this._rensaFlag;
	},

	/**
	 * 連鎖実行中フラグをセット
	 */
	setRensaFlag: function(flag){
		this._rensaFlag = flag;
	},

	/* ============================================================ */
	_selectedEditorUnit: TYPE_NONE,	// 選択中の編集ユニット

	/**
	 * 選択中の編集ユニットを取得
	 */
	getSelectedEditorUnit: function(){
		return this._selectedEditorUnit;
	},

	/**
	 * 選択中の編集ユニットをセット
	 */
	setSelectedEditorUnit: function(type){
		this._selectedEditorUnit = type;
	},

	/* ============================================================ */
	_nowRensa: 0,			// 現在の連鎖数
	_nowPoint: 0,			// 現在の得点
	_maxRensa: 0,			// 最大連鎖数
	_totalPoint: 0,			// 累計得点
	_handIndex: 0,			// 手数
	_nowEraseColor: 0,		// 現在の消色数
	_totalEraseColor: 0,	// 累計消色数
	_bitEraseColor: 0,		// 累計消色ビットフラグ
	_nowErasePuyo: [],		// 現在の消ぷよ数
	_totalErasePuyo: [],	// 累計消ぷよ数
	_nowEraseGroup: [],		// 現在の消組数
	_totalEraseGroup: [],	// 累計消組数
	_nazoClearFlag: false,	// なぞぷよクリアフラグ

	/**
	 * 現在の連鎖数を取得
	 */
	getNowRensa: function(){
		return this._nowRensa;
	},

	/**
	 * 現在の得点を取得
	 */
	getNowPoint: function(){
		return this._nowPoint;
	},

	/**
	 * 最大連鎖数を取得
	 */
	getMaxRensa: function(){
		return this._maxRensa;
	},

	/**
	 * 累計得点を取得
	 */
	getTotalPoint: function(){
		return this._totalPoint;
	},

	/**
	 * 手数を取得
	 */
	getHandIndex: function(){
		return this._handIndex;
	},

	/**
	 * 現在の消色数を取得
	 */
	getNowEraseColor: function(){
		return this._nowEraseColor;
	},

	/**
	 * 累計消色数を取得
	 */
	getTotalEraseColor: function(){
		return this._totalEraseColor;
	},

	/**
	 * 対象名を指定して現在の消数を取得
	 */
	_getEraseMemberNum: function(condPuyo, member){
		var list = NAZO_COND_PUYO_TYPE_LIST[condPuyo];
		var num = 0;
		for(var i = 0, len = list.length; i < len; i++){
			num += this[member][list[i]];
		}
		return num;
	},

	/**
	 * 現在の消ぷよ数を取得
	 */
	getNowErasePuyo: function(condPuyo){
		return this._getEraseMemberNum(condPuyo, '_nowErasePuyo');
	},

	/**
	 * 累計消ぷよ数を取得
	 */
	getTotalErasePuyo: function(condPuyo){
		return this._getEraseMemberNum(condPuyo, '_totalErasePuyo');
	},

	/**
	 * 現在の消組数を取得
	 */
	getNowEraseGroup: function(condPuyo){
		return this._getEraseMemberNum(condPuyo, '_nowEraseGroup');
	},

	/**
	 * 累計消組数を取得
	 */
	getTotalEraseGroup: function(condPuyo){
		return this._getEraseMemberNum(condPuyo, '_totalEraseGroup');
	},

	/**
	 * なぞぷよクリアフラグを取得
	 */
	getNazoClearFlag: function(){
		return this._nazoClearFlag;
	},

	/**
	 * スコアをリセット
	 */
	resetScore: function(){
		this._nowRensa = 0;
		this._nowPoint = 0;
		this._maxRensa = 0;
		this._totalPoint = 0;
		this._handIndex = 0;
		this._nowEraseColor = 0;
		this._totalEraseColor = 0;
		this._bitEraseColor = 0;
		for(var i = TYPE_COND_PUYO_BEGIN; i <= TYPE_COND_PUYO_END; i++){
			this._nowErasePuyo[i] = 0;
			this._totalErasePuyo[i] = 0;
			this._nowEraseGroup[i] = 0;
			this._totalEraseGroup[i] = 0;
		}
		this._nazoClearFlag = false;

		// 現在の状態を保存
		simData.tokoSavePresent();
	},

	/**
	 * 現在のスコアをリセット
	 */
	resetNowScore: function(){
		this._nowRensa = 0;
		this._nowPoint = 0;
		this._nowEraseColor = 0;
		for(var i = TYPE_COND_PUYO_BEGIN; i <= TYPE_COND_PUYO_END; i++){
			this._nowErasePuyo[i] = 0;
			this._nowEraseGroup[i] = 0;
		}
	},

	/**
	 * 連鎖数を1増やす
	 */
	incRensa: function(){
		this._nowRensa++;
		this._maxRensa = Math.max(this._maxRensa, this._nowRensa);
	},

	/**
	 * 得点を増やす
	 */
	addPoint: function(point){
		this._nowPoint += point;
		this._totalPoint += point;
	},

	/**
	 * 手数を1増やす
	 */
	incHandIndex: function(){
		this._handIndex++;
	},

	/**
	 * 消色数をセット
	 */
	setEraseColor: function(typeBit){
		this._bitEraseColor |= typeBit;
		this._nowEraseColor = 0;
		this._totalEraseColor = 0;
		for(var i = TYPE_PUYO_BEGIN; i <= TYPE_PUYO_END; i++){
			if((typeBit >> i) & 1){
				this._nowEraseColor++;
			}
			if((this._bitEraseColor >> i) & 1){
				this._totalEraseColor++;
			}
		}
	},

	/**
	 * 消ぷよ数をセット
	 */
	setErasePuyo: function(type, num){
		this._nowErasePuyo[type] = num;
		this._totalErasePuyo[type] += num;
	},

	/**
	 * 消組数をセット
	 */
	setEraseGroup: function(type, num){
		this._nowEraseGroup[type] = num;
		this._totalEraseGroup[type] += num;
	},

	/**
	 * なぞぷよクリアフラグをセット
	 */
	setNazoClearFlag: function(flag){
		this._nazoClearFlag = flag;
	},

	/* ============================================================ */
	_appearPuyo: [],		// とこぷよでの色ごとの出現フラグ
	_appearPuyoCopy: [],	// 色ごとの出現フラグのとこぷよ開始時のコピー

	/**
	 * とこぷよでの色ごとの出現フラグを取得
	 */
	getAppearPuyo: function(type){
		return this._appearPuyo[type];
	},

	/**
	 * とこぷよでの色ごとの出現フラグをセット
	 */
	setAppearPuyo: function(type, flag){
		this._appearPuyo[type] = (flag ? 1 : 0);
	},

	/**
	 * とこぷよでの色ごとの出現フラグを反転
	 */
	flipAppearPuyo: function(type){
		this._appearPuyo[type] ^= 1;
	},

	/**
	 * 出現するぷよの色数を取得する
	 */
	getAppearCount: function(){
		var count = 0;
		for(var i = TYPE_PUYO_BEGIN; i <= TYPE_PUYO_END; i++){
			count += this._appearPuyo[i];
		}
		return count;
	},

	/**
	 * とこぷよ開始時の色ごとの出現フラグを取得
	 */
	getAppearPuyoToko: function(type){
		return this._appearPuyoCopy[type];
	},

	/**
	 * 色ごとの出現フラグをとこぷよ用にコピーする
	 */
	copyAppearPuyoForToko: function(){
		for(var i = TYPE_PUYO_BEGIN; i <= TYPE_PUYO_END; i++){
			this._appearPuyoCopy[i] = this._appearPuyo[i];
		}
	},

	/* ============================================================ */
	_handData: [],												// 現在手の表示データ
	_handPuyo: [TYPE_NONE, TYPE_NONE, TYPE_NONE, TYPE_NONE],	// 現在手のぷよタイプ
	_handAxis: [HAND_START_X, HAND_START_Y],					// 現在手の軸ぷよの位置
	_handDir: [0, -1],											// 現在手の _handPuyo[2] から見た _handPuyo[0] の位置
	_handType: HAND_TYPE_NONE,									// 現在手の手タイプ
	_handOjama: [0, 0],											// 現在手のおじゃまぷよ
	_handDropHistory: [],										// 手の落下履歴

	/**
	 * 現在手の表示データを初期化
	 */
	initHandData: function(){
		for(var i = 0, len = (FIELD_X_MAX + 2) * 3; i < len; i++){
			this._handData[i] = TYPE_NONE;
		}
	},

	/**
	 * 現在手の表示データを取得
	 */
	getHandData: function(px, py){
		return this._handData[px + py * (FIELD_X_MAX + 2)];
	},

	/**
	 * 現在手の表示データをセット
	 */
	setHandData: function(px, py, type){
		this._handData[px + py * (FIELD_X_MAX + 2)] = type;
	},

	/**
	 * 現在手の手タイプを取得
	 */
	getHandType: function(){
		return this._handType;
	},

	/**
	 * 現在手のおじゃまぷよを取得
	 */
	getHandOjama: function(){
		return this._handOjama;
	},

	/**
	 * 手の落下履歴を初期化
	 */
	initHandDropHistory: function(){
		this._handDropHistory = [];
	},

	/**
	 * 手の落下履歴を取得
	 */
	getHandDropHistory: function(index){
		return this._handDropHistory[index];
	},

	/**
	 * 手の落下履歴をセット
	 */
	setHandDropHistory: function(index, handCondition){
		this._handDropHistory[index] = handCondition;
	},

	/**
	 * 現在手の状態を取得
	 */
	getHandCondition: function(){
		return {
			type: this._handType,
			axis: this._handAxis,
			dir: this._handDir,
			puyo: this._handPuyo,
			ojama: this._handOjama
		};
	},

	/**
	 * 次の現在手をセット
	 */
	_setNextHand: function(handPuyo, handOjama, handType){
		this._handPuyo = handPuyo;
		this._handAxis = [HAND_START_X, HAND_START_Y];
		this._handDir = [0, -1];
		this._handType = handType;
		this._handOjama = handOjama;
	},

	/**
	 * 現在手の状態から表示データ全体を更新
	 */
	updateHandData: function(){
		for(var py = 0; py < 3; py++){
			for(var px = 1; px < FIELD_X_MAX + 1; px++){
				simData.setHandData(px, py, TYPE_NONE);
			}
		}
		simData.setHandData(this._handAxis[0] + this._handDir[0]                   , this._handAxis[1] + this._handDir[1]                   , this._handPuyo[0]);
		simData.setHandData(this._handAxis[0] + this._handDir[0] - this._handDir[1], this._handAxis[1] + this._handDir[1] + this._handDir[0], this._handPuyo[1]);
		simData.setHandData(this._handAxis[0]                                      , this._handAxis[1]                                      , this._handPuyo[2]);
		simData.setHandData(this._handAxis[0]                    - this._handDir[1], this._handAxis[1]                    + this._handDir[0], this._handPuyo[3]);
	},

	/**
	 * ぷよ譜の手状態から現在手をセット
	 */
	setHandByCondition: function(handCondition){
/*
// でかぷよは設置したときの色で履歴に保存している
		if(handCondition.type == HAND_TYPE_4B){
			// でかぷよの場合は色変え
			for(var i = 0; i < HAND_PUYO_NUM_MAX; i++){
				this._handPuyo[i] = handCondition.puyo[0];
			}
		}
*/
		this._handType = handCondition.type;
		this._handAxis = handCondition.axis;
		this._handDir = handCondition.dir;
		this._handOjama = handCondition.ojama;
		this._handPuyo = handCondition.puyo; // add
		simData.updateHandData();
	},

	/**
	 * 現在手のX座標の範囲を取得
	 */
	_getHandPosX: function(){
		var posXList = [
			this._handAxis[0] + this._handDir[0],
			this._handAxis[0] + this._handDir[0] - this._handDir[1],
			this._handAxis[0],
			this._handAxis[0] - this._handDir[1]
		];
		var minPosX = FIELD_X_MAX + 1;
		var maxPosX = 0;
		for(var i = 0; i < HAND_PUYO_NUM_MAX; i++){
			if(this._handPuyo[i] != TYPE_NONE){
				minPosX = Math.min(minPosX, posXList[i]);
				maxPosX = Math.max(maxPosX, posXList[i]);
			}
		}
		return {min:minPosX, max:maxPosX};
	},

	/**
	 * 現在手が横移動可能かチェック
	 */
	_checkHandMovable: function(dir){
		var posX = this._getHandPosX();
		if((dir < 0) && (posX.min <= 1)){
			// 左に移動できない
			return false;
		}
		if((dir > 0) && (posX.max >= FIELD_X_MAX)){
			// 右に移動できない
			return false;
		}
		return true;
	},

	/**
	 * 現在手を横移動
	 */
	moveHand: function(dir){
		if(dir == 0){
			return false;
		}
		if(!this._checkHandMovable(dir)){
			return false;
		}
		this._handAxis[0] += ((dir > 0) ? 1 : -1);
		return true;
	},

	/**
	 * でかぷよの色変え
	 */
	_rotateDekapuyo: function(dir){
		// でかぷよの色順
		var dekapuyoColors = [];
		for(var i = TYPE_PUYO_BEGIN; i <= TYPE_PUYO_END; i++){
			dekapuyoColors.push(i);
		}

		// 現在の色を取得
		var defaultIndex = -1;
		for(var i = 0, len = dekapuyoColors.length; i < len; i++){
			if(dekapuyoColors[i] == this._handPuyo[0]){
				defaultIndex = i;
				break;
			}
		}
		if(defaultIndex < 0){
			return;
		}

		// 色を変更
		var index = defaultIndex;
		while(1){
			// 色を一つずらす
			index = (index + ((dir > 0) ? 1 : -1) + dekapuyoColors.length) % dekapuyoColors.length;
			if(index == defaultIndex){
				// 1巡したら変更しない
				break;
			}
			if(simData.getAppearPuyoToko(dekapuyoColors[index])){
				// 出現する色なら決定
				break;
			}
		}
		for(var i = 0; i < HAND_PUYO_NUM_MAX; i++){
			this._handPuyo[i] = dekapuyoColors[index];
		}
	},

	/**
	 * 現在手を回転
	 */
	rotateHand: function(dir){
		if(dir == 0){
			return false;
		}
		if(this._handType == HAND_TYPE_4B){
			// でかぷよは色変えのみ
			this._rotateDekapuyo(dir);
			return true;
		}
		if(this._handType == HAND_TYPE_4A){
			// 4個ぷよ2色は軸ぷよも同時に移動
			if(dir > 0){
				this._handAxis[0] += this._handDir[0];
				this._handAxis[1] += this._handDir[1];
			} else {
				this._handAxis[0] -= this._handDir[1];
				this._handAxis[1] += this._handDir[0];
			}
		}
		this._handDir = [this._handDir[1] * ((dir > 0) ? -1 : 1), this._handDir[0] * ((dir > 0) ? 1 : -1)];
		var posX = this._getHandPosX();
		if(posX.min < 1){
			// 左の壁蹴り
			this._handAxis[0] += 1 - posX.min;
		} else if(posX.max > FIELD_X_MAX){
			// 右の壁蹴り
			this._handAxis[0] -= posX.max - FIELD_X_MAX;
		}
		return true;
	},

	/**
	 * 現在手を落下
	 */
	dropHand: function(){
		// 現在手の下の段から順番に落下処理を行う
		var droppable = false;
		for(var py = 2; py >= 0; py--){
			for(var px = 1; px < FIELD_X_MAX + 1; px++){
				var type = simData.getHandData(px, py);
				if(type == TYPE_NONE){
					continue;
				}
				// フィールドの同じ列を上から順番に見て、空じゃないユニットがあればその上に配置する
				// 既に一番上まで配置されている場合は消滅させる
				for(var fpy = 0; fpy < FIELD_Y_MAX + 2; fpy++){
					if(simData.getFieldDataByPos(px, fpy) != TYPE_NONE){
						if(fpy > 0){
							simData.setFieldDataByPos(px, fpy - 1, type);
							droppable = true;
						}
						break;
					}
				}
			}
		}
		return droppable;
	},

	/**
	 * おじゃまぷよを落下
	 */
	dropOjama: function(ojama){
		var isDrop;
		for(var px = 1; px < FIELD_X_MAX + 1; px++){
			var num = Math.min((ojama[0] + ((ojama[1] >> (px - 1)) & 1)), OJAMA_ROW_MAX);

			// フィールドの同じ列を上から順番に見て、空じゃないユニットがあればその上に配置する
			for(var fpy = 0; fpy < FIELD_Y_MAX + 2; fpy++){
				isDrop = false;
				if(simData.getFieldDataByPos(px, fpy) != TYPE_NONE){
					isDrop = true;
					for(var fpy2 = fpy - 1, end = Math.max(0, fpy - num); fpy2 >= end; fpy2--){
						simData.setFieldDataByPos(px, fpy2, TYPE_OJAMA);
					}
				}
				if(isDrop){
					break;
				}
			}
		}
	},

	/**
	 * おじゃまぷよの数値を調整する
	 */
	adjustOjama: function(ojama){
		var row = ojama[0];
		var pos = ojama[1];
		if(pos == 63){	// 2^6-1
			row++;
			pos = 0;
		}
		if(row >= OJAMA_ROW_MAX){
			row = OJAMA_ROW_MAX;
			pos = 0;
		}
		return [row, pos];
	},

	/* ============================================================ */
	_nextData: [],			// ネクストの表示データ
	_nextOjama: [],			// ネクストのおじゃまぷよデータ
	_nextPattern: [],		// ネクストの出現パターン
	_nextPatternIndex: 0,	// ネクストの出現パターンの参照位置
	_colorHistory: [],		// 出現色リスト
	_colorHistoryIndex: 0,	// 出現色リストの参照位置

	/**
	 * ネクストの表示データを取得
	 */
	getNextData: function(index){
		return this._nextData[index];
	},

	/**
	 * ネクストの表示データをインデックス指定でセット
	 */
	_setNextData: function(index, nextData){
		this._nextData[index] = nextData;
	},

	/**
	 * ネクストのおじゃまぷよデータを取得
	 */
	getNextOjama: function(index){
		return this._nextOjama[index];
	},

	/**
	 * ネクストのおじゃまぷよデータをインデックス指定でセット
	 */
	_setNextOjama: function(index, nextOjama){
		this._nextOjama[index] = nextOjama;
	},

	/**
	 * ネクストの表示データ、おじゃまぷよデータをインデックス指定でセット
	 */
	_setNextAll: function(index, nextData, nextOjama){
		this._setNextData(index, nextData);
		//this._setNextOjama(index, simData.adjustOjama(nextOjama));
	},

	/**
	 * ネクストの出現パターンをセット
	 */
	setNextPattern: function(pattern){
		this._nextPattern = pattern;
	},

	/**
	 * ネクストの出現パターンの参照位置をセット
	 */
	setNextPatternIndex: function(index){
		this._nextPatternIndex = index;
	},

	/**
	 * 出現色リストを初期化
	 */
	initColorHistory: function(){
		this._colorHistory = [];
	},

	/**
	 * 出現色リストを取得
	 */
	getColorHistory: function(num){
		return this._colorHistory[num % this._colorHistory.length];
	},

	/**
	 * 出現色リストをセット
	 */
	setColorHistory: function(num, pair){
		this._colorHistory[num] = pair;
	},

	/**
	 * 出現色リストにデータを追加
	 */
	pushColorHistory: function(pair){
		this._colorHistory.push(pair);
	},

	/**
	 * 出現色リストの参照位置をセット
	 */
	setColorHistoryIndex: function(index){
		this._colorHistoryIndex = index;
	},

	/**
	 * ネクストの出現パターンを参照位置から取得し、参照位置を一つ進める
	 */
	_getNextPatternOne: function(){
		var handType = this._nextPattern[this._nextPatternIndex % this._nextPattern.length];
		this._nextPatternIndex++;
		return handType;
	},

	/**
	 * 現在の手タイプを取得
	 */
	getNowHandType: function(){
		return this._nextPattern[(this._nextPatternIndex - NEXT_MAX + this._nextPattern.length) % this._nextPattern.length];
	},

	/**
	 * 4個ぷよ2色が同色だった場合に新しい色を取得する
	 */
	_getNewColorHandType4A: function(historyIndex, colors){
		if(colors[0] != colors[1]){
			return colors[1];
		}

		// 出現するぷよの色を取得
		var appearList = [];
		for(var i = TYPE_PUYO_BEGIN; i <= TYPE_PUYO_END; i++){
			if(i == colors[0]){
				// 現在の色は対象外
				continue;
			}
			if(simData.getAppearPuyoToko(i)){
				appearList.push(i);
			}
		}
		if(appearList.length == 0){
			return colors[1];
		}
		// historyIndex の値を利用してランダム風に色を決定する
		return appearList[historyIndex % appearList.length];
	},

	/**
	 * ネクストの表示データの1手分を設定する
	 */
	setNextOne: function(nextIndex, historyIndex){
		var colors = simData.getColorHistory(historyIndex);
		switch(this._getNextPatternOne()){
			case HAND_TYPE_2:
				// 2個ぷよ
				this._setNextAll(nextIndex, [colors[1], TYPE_NONE, colors[0], TYPE_NONE], [0, 0]);
				break;
			case HAND_TYPE_3A:
				// 3個ぷよ縦型
				this._setNextAll(nextIndex, [colors[0], TYPE_NONE, colors[0], colors[1]], [0, 0]);
				break;
			case HAND_TYPE_3B:
				// 3個ぷよ横型
				this._setNextAll(nextIndex, [colors[1], TYPE_NONE, colors[0], colors[0]], [0, 0]);
				break;
			case HAND_TYPE_4A:
				// 4個ぷよ2色
				if(colors[0] == colors[1]){
					// 同色だった場合は色を変える
					colors[1] = this._getNewColorHandType4A(historyIndex, colors);
					simData.setColorHistory(historyIndex, colors);
				}
				this._setNextAll(nextIndex, [colors[0], colors[1], colors[0], colors[1]], [0, 0]);
				break;
			case HAND_TYPE_4B:
				// でかぷよ
				this._setNextAll(nextIndex, [colors[0], colors[0], colors[0], colors[0]], [0, 0]);
				break;
			case HAND_TYPE_OJ:
				// おじゃまぷよ
				this._setNextAll(nextIndex, [TYPE_OJAMA, TYPE_NONE, TYPE_NONE, TYPE_NONE], [
						simData.getNextSettingOjamaRow(historyIndex),
						simData.getNextSettingOjamaPos(historyIndex)
					]);
				break;
		}
	},

	/**
	 * ネクストの表示データを1手進める
	 */
	forwardNext: function(){
		this._setNextHand(simData.getNextData(0), simData.getNextOjama(0), simData.getNowHandType());
		for(var i = 0; i < NEXT_MAX - 1; i++){
			this._setNextAll(i, simData.getNextData(i + 1), simData.getNextOjama(i + 1));
		}
		simData.setNextOne(NEXT_MAX - 1, this._colorHistoryIndex + NEXT_MAX);
		this._colorHistoryIndex++;
		this._handIndex++;
	},

	/* ============================================================ */
	_fieldData: [],		// フィールドの表示データ
	_fieldDataCopy: [],	// フィールドの表示データのコピー

	/**
	 * フィールドの表示データを初期化
	 */
	initFieldData: function(){
		for(var i = 0, len = (FIELD_X_MAX + 2) * (FIELD_Y_MAX + 2); i < len; i++){
			this._fieldData[i] = TYPE_NONE;
			this._fieldDataCopy[i] = TYPE_NONE;
		}
	},

	/**
	 * フィールドの表示データをクリア
	 */
	clearFieldData: function(){
		for(var py = 0; py < FIELD_Y_MAX + 1; py++){
			for(var px = 1; px < FIELD_X_MAX + 1; px++){
				this._fieldData[px + py * (FIELD_X_MAX + 2)] = TYPE_NONE;
			}
		}
	},

	/**
	 * フィールドの表示データを取得
	 */
	getFieldData: function(index){
		return this._fieldData[index];
	},

	/**
	 * 位置を指定して、フィールドの表示データを取得
	 */
	getFieldDataByPos: function(px, py){
		return this._fieldData[px + py * (FIELD_X_MAX + 2)];
	},

	/**
	 * フィールドの表示データをセット
	 */
	setFieldData: function(index, type){
		this._fieldData[index] = type;
	},

	/**
	 * 位置を指定して、フィールドの表示データをセット
	 */
	setFieldDataByPos: function(px, py, type){
		this._fieldData[px + py * (FIELD_X_MAX + 2)] = type;
	},

	/**
	 * フィールドの表示データをバックアップ
	 */
	buckupFieldData: function(){
		for(var py = 0; py < FIELD_Y_MAX + 1; py++){
			for(var px = 1; px < FIELD_X_MAX + 1; px++){
				var index = px + py * (FIELD_X_MAX + 2);
				this._fieldDataCopy[index] = this._fieldData[index];
			}
		}
	},

	/**
	 * バックアップしたフィールドの表示データを復元
	 */
	returnFieldData: function(){
		for(var py = 0; py < FIELD_Y_MAX + 1; py++){
			for(var px = 1; px < FIELD_X_MAX + 1; px++){
				var index = px + py * (FIELD_X_MAX + 2);
				this._fieldData[index] = this._fieldDataCopy[index];
			}
		}
	},

	/**
	 * なぞぷよクリア条件の【ぷよ】がフィールドから全て消えているかチェック
	 */
	checkFieldAllEraseForNazo: function(condPuyo){
		var list = NAZO_COND_PUYO_TYPE_LIST[condPuyo];
		var listLen = list.length;
		for(var py = 0; py < FIELD_Y_MAX + 1; py++){
			for(var px = 1; px < FIELD_X_MAX + 1; px++){
				var type = this._fieldData[px + py * (FIELD_X_MAX + 2)];
				for(var i = 0; i < listLen; i++){
					if(type == list[i]){
						// 残っている
						return false;
					}
				}
			}
		}
		// 全て消えている
		return true;
	},

	/* ============================================================ */
	_nextSettingData: [],		// ネクスト設定の表示データ
	_nextSettingType: [],		// ネクスト設定の手タイプ
	_nextSettingOjamaRow: [],	// ネクスト設定のおじゃまぷよ段数
	_nextSettingOjamaPos: [],	// ネクスト設定のおじゃまぷよ端数落下位置（ビットフラグ）

	/**
	 * ネクスト設定の表示データを取得
	 */
	getNextSettingData: function(index, j){
		return this._nextSettingData[index][j];
	},

	/**
	 * ネクスト設定の表示データをセット
	 */
	setNextSettingData: function(index, j, type){
		this._nextSettingData[index][j] = type;
	},

	/**
	 * ネクスト設定の手タイプを取得
	 */
	getNextSettingType: function(index){
		return this._nextSettingType[index];
	},

	/**
	 * ネクスト設定の手タイプをセット
	 */
	setNextSettingType: function(index, handType){
		this._nextSettingType[index] = handType;
	},

	/**
	 * ネクスト設定のおじゃまぷよ段数を取得
	 */
	getNextSettingOjamaRow: function(index){
		return this._nextSettingOjamaRow[index];
	},

	/**
	 * ネクスト設定のおじゃまぷよ段数をセット
	 */
	setNextSettingOjamaRow: function(index, num){
		this._nextSettingOjamaRow[index] = num;
	},

	/**
	 * ネクスト設定のおじゃまぷよ端数落下位置を取得
	 */
	getNextSettingOjamaPos: function(index){
		return this._nextSettingOjamaPos[index];
	},

	/**
	 * ネクスト設定のおじゃまぷよ端数落下位置をセット
	 */
	setNextSettingOjamaPos: function(index, num){
		this._nextSettingOjamaPos[index] = num;
	},

	/**
	 * ネクスト設定の手タイプを変更
	 */
	changeNextSettingType: function(index){
		var typeList = [];
		typeList[HAND_TYPE_2]  = HAND_TYPE_3A;
		typeList[HAND_TYPE_3A] = HAND_TYPE_3B;
		typeList[HAND_TYPE_3B] = HAND_TYPE_4A;
		typeList[HAND_TYPE_4A] = HAND_TYPE_4B;
		typeList[HAND_TYPE_4B] = HAND_TYPE_OJ;
		typeList[HAND_TYPE_OJ] = HAND_TYPE_2;

		this._nextSettingType[index] = typeList[this._nextSettingType[index]];
	},

	/**
	 * ネクスト設定のおじゃまぷよ段数を変更
	 */
	changeNextSettingOjamaRow: function(index){
		return this._nextSettingOjamaRow[index] = (this._nextSettingOjamaRow[index] + 1) % (OJAMA_ROW_MAX + 1);
	},

	/**
	 * ネクスト設定のおじゃまぷよ端数落下位置を変更
	 */
	changeNextSettingOjamaPos: function(index, j){
		var bit = ((this._nextSettingOjamaPos[index] >> j) & 1) ^ 1;
		if(bit){
			this._nextSettingOjamaPos[index] |= (1 << j);
		} else {
			this._nextSettingOjamaPos[index] &= ~(1 << j);
		}
		return bit;
	},

	/**
	 * ネクスト設定を初期化
	 */
	initNextSettingAll: function(){
		this._nextSettingData = [];
		this._nextSettingType = [];
		this._nextSettingOjamaRow = [];
		this._nextSettingOjamaPos = [];
		for(var i = 0; i < NEXT_SETTING_NUM; i++){
			this._nextSettingData[i] = [TYPE_NONE, TYPE_NONE, TYPE_NONE, TYPE_NONE];
			this._nextSettingType[i] = HAND_TYPE_2;
			this._nextSettingOjamaRow[i] = 0;
			this._nextSettingOjamaPos[i] = 0;
		}
	},

	/* ============================================================ */
	_puyoHistory: [],		// ぷよ譜データ
	_puyoHistoryIndex: 0,	// ぷよ譜データの参照位置
	_updateAfterDropFlag: false,	// 落下処理後の現在手更新フラグ

	/**
	 * ぷよ譜データを初期化
	 */
	initPuyoHistoryData: function(){
		this._puyoHistory = [];
		this._puyoHistoryIndex = 0;
	},

	/**
	 * ぷよ譜データを取得
	 */
	getPuyoHistory: function(index){
		return this._puyoHistory[index];
	},

	/**
	 * ぷよ譜データをセット
	 */
	setPuyoHistory: function(index, handCondition){
		this._puyoHistory[index] = handCondition;
	},

	/**
	 * ぷよ譜データの要素数を取得
	 */
	getPuyoHistoryLength: function(){
		return this._puyoHistory.length;
	},

	/**
	 * ぷよ譜データの参照位置を1減らす
	 */
	decPuyoHistoryIndex: function(){
		if(this._puyoHistoryIndex > 0){
			this._puyoHistoryIndex--;
		}
	},

	/**
	 * 落下処理後の現在手更新フラグを取得
	 */
	getUpdateAfterDropFlag: function(){
		return this._updateAfterDropFlag;
	},

	/**
	 * 落下処理後の現在手更新フラグをセット
	 */
	setUpdateAfterDropFlag: function(flag){
		this._updateAfterDropFlag = flag;
	},

	/**
	 * ぷよ譜データが最後まで進んでいるか
	 */
	isFinishedPuyoHistory: function(){
		return (typeof this._puyoHistory[this._puyoHistoryIndex] == 'undefined');
	},

	/**
	 * ぷよ譜データを1手進める
	 */
	forwardPuyoHistory: function(){
		if(this._puyoHistoryIndex >= this._puyoHistory.length){
			return false;
		}
		return this._puyoHistory[this._puyoHistoryIndex++];
	},

	/**
	 * 現在手をぷよ譜データで更新する
	 */
	updateHandByPuyoHistory: function(){
		if(this._puyoHistoryIndex >= this._puyoHistory.length){
			return;
		}
		simData.setHandByCondition(this._puyoHistory[this._puyoHistoryIndex]);
	},

	/* ============================================================ */
	_tokoSaveDataList: [],	// とこぷよ保存データリスト（i手目を[i]に保存）
	_tokoSaveItemList: [	// 保存するデータ項目
		'_nowRensa',
		'_nowPoint',
		'_maxRensa',
		'_totalPoint',
		'_handIndex',
		'_nowEraseColor',
		'_totalEraseColor',
		'_bitEraseColor',
		'_nowErasePuyo',
		'_totalErasePuyo',
		'_nowEraseGroup',
		'_totalEraseGroup',
		'_nazoClearFlag',
		'_appearPuyo',
		'_appearPuyoCopy',
		'_handData',
		'_handPuyo',
		'_handAxis',
		'_handDir',
		'_handType',
		'_handOjama',
		'_handDropHistory',
		'_nextData',
		'_nextOjama',
		'_nextPattern',
		'_nextPatternIndex',
		'_colorHistory',
		'_colorHistoryIndex',
		'_fieldData',
		'_fieldDataCopy'
	],

	/**
	 * 1手の1項目を保存
	 */
	_tokoSaveMember: function(index, member){
		if(typeof this[member] == 'object'){
			this._tokoSaveDataList[index][member] = [];
			for(var i in this[member]){
				this._tokoSaveDataList[index][member][i] = this[member][i];
			}
		} else {
			this._tokoSaveDataList[index][member] = this[member];
		}
	},

	/**
	 * 1手の1項目を読み込み
	 */
	_tokoLoadMember: function(index, member){
		if(typeof this[member] == 'object'){
			this[member] = [];
			for(var i in this._tokoSaveDataList[index][member]){
				this[member][i] = this._tokoSaveDataList[index][member][i];
			}
		} else {
			this[member] = this._tokoSaveDataList[index][member];
		}
	},

	/**
	 * 1手を保存
	 */
	tokoSave: function(index){
		this._tokoSaveDataList[index] = {};
		for(var i = 0, len = this._tokoSaveItemList.length; i < len; i++){
			this._tokoSaveMember(index, this._tokoSaveItemList[i]);
		}
	},

	/**
	 * 1手を読み込み
	 */
	tokoLoad: function(index){
		if(typeof this._tokoSaveDataList[index] == 'undefined'){
			return;
		}
		for(var i = 0, len = this._tokoSaveItemList.length; i < len; i++){
			this._tokoLoadMember(index, this._tokoSaveItemList[i]);
		}
		simView.setScoreText();
		simView.redrawHand();
		simView.redrawNext();
		simView.redrawFieldImg();
	},

	/**
	 * 現在の状態を保存
	 */
	tokoSavePresent: function(){
		simData.tokoSave(this._handIndex);
		this._tokoSaveDataList.splice(this._handIndex + 1, this._tokoSaveDataList.length - (this._handIndex + 1));	// end を指定しないとIEで動かなかった
	},

	/**
	 * 1手戻る
	 */
	tokoBack: function(){
		if(this._handIndex > 0){
			simData.tokoLoad(this._handIndex - 1);
		}
	},

	/**
	 * 1手進む
	 */
	tokoForward: function(){
		if(this._handIndex < this._tokoSaveDataList.length - 1){
			simData.tokoLoad(this._handIndex + 1);
		}
	},

	/* ============================================================ */
	/**
	 * 
	 */
	funcname: function(){
	},

	_dummy: function(){}
};

/**
 * 関数定義 - 共通
 */
	/**
	 * 乱数を取得する
	 */
	function getRandom(min, max){
		return parseInt(Math.random() * (max + 1 - min)) + min;
	}

	/**
	 * ユーザーエージェントを見てスマートフォン(iPad含む)か判定する
	 */
	function isSmartPhone(){
		var ua = navigator.userAgent;
		if(ua.indexOf('iPhone') >= 0){
			return true;
		}
		if(ua.indexOf('iPod') >= 0){
			return true;
		}
		if(ua.indexOf('iPad') >= 0){
			return true;
		}
		if(ua.indexOf('Android') >= 0){
			return true;
		}
		return false;
	}

/**
 * 関数定義 - 連鎖
 */
var simRensa = {
	/**
	 * 連鎖チェックを行う
	 */
	rensaCheck: function(){
		if(simData.getRensaFlag()){
			return;
		}
		simData.setRensaFlag(true);
		this._rensaDrop(false);
	},

	/**
	 * 連鎖のステップ実行を行う
	 */
	rensaStep: function(){
		if(simData.getRensaFlag()){
			return;
		}
		this._rensaDrop(true);
	},

	/**
	 * 連鎖の落下処理を行う
	 */
	_rensaDrop: function(isStep){
		// 各ユニットの落下数をカウント
		var dropNum = [];	// 各ユニットの落下数
		var isDrop = false;	// 落下するユニットがあるか
		(function(){
			// 各ユニットの落下数を初期化する
			for(var i = 0, len = (FIELD_X_MAX + 2) * (FIELD_Y_MAX + 2); i < len; i++){
				dropNum[i] = 0;
			}

			// 下の段から順番にユニットの落下数をカウントする
			for(var py = FIELD_Y_MAX; py >= 0; py--){
				for(var px = 1; px < FIELD_X_MAX + 1; px++){
					var index = px + py * (FIELD_X_MAX + 2);
					var type = simData.getFieldData(index);
					if(type == TYPE_KABE){
						// 壁は落下しない
						dropNum[index] = 0;
						continue;
					}

					var underIndex = px + (py + 1) * (FIELD_X_MAX + 2);
					var underType = simData.getFieldData(underIndex);
					if(underType == TYPE_KABE){
						// 下にあるユニットが壁の場合は落下しない
						dropNum[index] = 0;
						continue;
					}

					dropNum[index] = dropNum[underIndex];
					if(underType == TYPE_NONE){
						dropNum[index]++;
						if(type != TYPE_NONE){
							isDrop = true;
						}
					}
				}
			}
		})();

		if(!isDrop){
			// 落下なしの場合
			// フィールドの表示を更新（直前に消去処理があった場合のために必要）
			//simView.redrawFieldImg();

			// 連鎖の消去処理
			this._rensaErase(isStep);

			return;
		}

		// フィールドに落下を反映
		for(var py = FIELD_Y_MAX; py >= 0; py--){
			for(var px = 1; px < FIELD_X_MAX + 1; px++){
				var index = px + py * (FIELD_X_MAX + 2);
				if(dropNum[index] > 0){
					simData.setFieldDataByPos(px, py + dropNum[index], simData.getFieldData(index));
					simData.setFieldData(index, TYPE_NONE);
				}
			}
		}

		// フィールドの表示を更新
		//simView.redrawFieldImg();

		if(!isStep){
			// ステップ実行じゃない場合
			var self = this;
			setTimeout(function(){
				// 連鎖の消去処理
				self._rensaErase(false);
			}, 3/*simView.getRensaSpeed()*/ * 100);
		}
	},

	/**
	 * 連鎖の消去処理を行う
	 */
	_rensaErase: function(isStep){
		var fieldCheckMain = [];	// 各ユニットの連結判定済フラグ
		var fieldCheckTemp = [];	// 各ユニットの一時的な連結判定済フラグ
		var connectNum = [];		// 各ユニットの連結数
		for(var i = 0, len = (FIELD_X_MAX + 2) * (FIELD_Y_MAX + 2); i < len; i++){
			fieldCheckMain[i] = false;
			fieldCheckTemp[i] = false;
			connectNum[i] = 0;
		}

		// ぷよが消える連結数を取得
        //var erase_num = simView.getEraseNum();
        var erase_num = 4;

		// 得点計算用データ
		var basePoint = 0;
		var eraseColor = 0;		// ビットフラグ
		var eraseCombi = [];
		var erasePuyo = [];
		var eraseGroup = [];
		for(var i = TYPE_COND_PUYO_BEGIN; i <= TYPE_COND_PUYO_END; i++){
			erasePuyo[i] = 0;
			eraseGroup[i] = 0;
		}

		// 連結数を取得するための再帰関数
		function _rensaCheckConnect(px, py, type){
			var index = px + py * (FIELD_X_MAX + 2);

			if(fieldCheckTemp[index]){
				// 連結判定済のユニットはカウントしない
				return 0;
			}
			if(type != simData.getFieldData(index)){
				// 色が異なるユニットはカウントしない
				return 0;
			}

			fieldCheckTemp[index] = true;	// このユニットを連結判定済にする

			// 隣接ユニットの連結数の合計を自身の連結数として返す
			var connect = 1;	// 自分自身
			if(py > 1){
				connect += _rensaCheckConnect(px, py - 1, type);
			}
			connect += _rensaCheckConnect(px, py + 1, type);
			connect += _rensaCheckConnect(px - 1, py, type);
			connect += _rensaCheckConnect(px + 1, py, type);

			return connect;
		}

		// 連結判定
		var isErase = false;	// 消えるぷよがあるか
		for(var py = 1; py < FIELD_Y_MAX + 1; py++){
			for(var px = 1; px < FIELD_X_MAX + 1; px++){
				var index = px + py * (FIELD_X_MAX + 2);
				var type = simData.getFieldData(index);
				if((TYPE_PUYO_BEGIN <= type) && (type <= TYPE_PUYO_END) && (!fieldCheckMain[index])){
					// まだ連結判定を行ってない色ぷよのみ対象
					var connect = _rensaCheckConnect(px, py, type);	// 連結数を取得
					if(connect >= erase_num){
						// このぷよが消える
						basePoint += connect * 10;
						eraseColor |= (1 << type);
						eraseCombi.push(connect);
						erasePuyo[type] += connect;
						eraseGroup[type]++;
						isErase = true;
					}
					// 連結数を連結しているぷよ全部に反映
					for(var i = 0, len = fieldCheckTemp.length; i < len; i++){
						if(fieldCheckTemp[i]){
							connectNum[i] = connect;
							fieldCheckMain[i] = true;
							fieldCheckTemp[i] = false;
						}
					}
				}
			}
		}

        //var nazoCond = simView.getNazoCond();	// なぞぷよクリア条件
        var nazoCond = 0;
		if(!isErase){
			// 消去なしの場合
			simData.setRensaFlag(false);
			if(simData.getDispMode() == MODE_TOKOPUYO){
				// とこぷよモード
				// なぞぷよクリア判定（「【n】連鎖する」を含む条件）
				if((nazoCond == NAZO_COND_N_EQ_RENSA) || (nazoCond == NAZO_COND_N_EQ_RENSA_P_ALL)){
					this._nazoClearCheck();
				}
				simView.redrawFieldImg();	// 現在手の落下位置を表示
				simData.tokoSavePresent();	// 現在の状態を保存
				if(simData.getUpdateAfterDropFlag()){
					simToko.redrawHandByPuyoHistory();
					simData.setUpdateAfterDropFlag(false);
				}
			}
			return;
		}

        var ojamaPoint = 0; //simView.getOjamaPoint();
        var kataPoint = 0; //simView.getKataPoint();
        var kataPoint2 = 0; //simView.getKataPoint2();

		// 消去処理
		for(var py = 1; py < FIELD_Y_MAX + 1; py++){
			for(var px = 1; px < FIELD_X_MAX + 1; px++){
				var index = px + py * (FIELD_X_MAX + 2);
				var type = simData.getFieldData(index);
				if(connectNum[index] >= erase_num){
					// ぷよ消去
					//simView.setFieldImgErase(px, py);
					simData.setFieldData(index, TYPE_NONE);
				} else if((TYPE_OJAMA_BEGIN <= type) && (type <= TYPE_OJAMA_END)){
					// おじゃまぷよ消去判定
					var ojamaDamage = ((connectNum[px + (py - 1) * (FIELD_X_MAX + 2)] >= erase_num) ? 1 : 0)
					                + ((connectNum[px + (py + 1) * (FIELD_X_MAX + 2)] >= erase_num) ? 1 : 0)
					                + ((connectNum[(px - 1) + py * (FIELD_X_MAX + 2)] >= erase_num) ? 1 : 0)
					                + ((connectNum[(px + 1) + py * (FIELD_X_MAX + 2)] >= erase_num) ? 1 : 0);
					if(ojamaDamage > 0){
						if((type == TYPE_KATA) && (ojamaDamage == 1)){
							// 固ぷよ→おじゃまぷよ
							basePoint += kataPoint;
							simView.setFieldImg(px, py, TYPE_OJAMA);
						} else {
							// おじゃまぷよ消去
							if(type == TYPE_OJAMA){
								basePoint += ojamaPoint;
							} else if(type == TYPE_KATA){
								basePoint += kataPoint2;
							}
							erasePuyo[type]++;
							simView.setFieldImgErase(px, py);
							simData.setFieldData(index, TYPE_NONE);
						}
					}
				}
			}
		}

		// 連鎖数を1増やす
		simData.incRensa();

		// 得点計算タイプを決定する
		var rensaBonus;
		var colorBonus;
		var combiBonus;
		if(true/*simView.getRule() == 'tsu'*/){
			// 通
			rensaBonus = RENSA_BONUS_LIST[0];
			colorBonus = COLOR_BONUS_LIST[0];
			combiBonus = COMBI_BONUS_LIST[0];
		} else {
			// フィーバー
			rensaBonus = FEVER_CHARA_LIST[simView.getSeries()][simView.getCulcChara()][simView.getRensaMode()];
			colorBonus = COLOR_BONUS_LIST[1];
			combiBonus = COMBI_BONUS_LIST[1];
		}

		// 連鎖ボーナスを取得
		var nowRensa = simData.getNowRensa();
		var rensaB = (nowRensa < rensaBonus.length) ? rensaBonus[nowRensa] : rensaBonus[rensaBonus.length - 1];

		// 多色ボーナスを取得
		var colorCount = 0;
		for(var i = TYPE_PUYO_BEGIN; i <= TYPE_PUYO_END; i++){
			if(eraseColor & (1 << i)){
				colorCount++;
			}
		}
		var colorB = (colorCount < colorBonus.length) ? colorBonus[colorCount] : colorBonus[colorBonus.length - 1];

		// 連結ボーナスを取得
		var combiB = 0;
		for(var i = 0, len = eraseCombi.length; i < len; i++){
			combiB += (eraseCombi[i] < combiBonus.length) ? combiBonus[eraseCombi[i]] : combiBonus[combiBonus.length - 1];
		}

		// この連鎖での得点を計算する
		simData.addPoint(basePoint * Math.max((rensaB + colorB + combiB), 1));

		// なぞぷよ用データ更新
		simData.setEraseColor(eraseColor);
		for(var i = TYPE_COND_PUYO_BEGIN; i <= TYPE_COND_PUYO_END; i++){
			simData.setErasePuyo(i, erasePuyo[i]);
			simData.setEraseGroup(i, eraseGroup[i]);
		}

		// なぞぷよクリア判定（「【n】連鎖する」を含まない条件）
		if((nazoCond != NAZO_COND_NONE) && (nazoCond != NAZO_COND_N_EQ_RENSA) && (nazoCond != NAZO_COND_N_EQ_RENSA_P_ALL)){
			this._nazoClearCheck();
		}

		// スコア表示を更新
		//simView.setScoreText();

		if(!isStep){
			// ステップ実行じゃない場合
			var self = this;
			setTimeout(function(){
				// 連鎖の落下処理
				self._rensaDrop(false);
			}, 3/*simView.getRensaSpeed()*/ * 100);
		}
	},

	/**
	 * なぞぷよクリア判定を行う
	 */
	_nazoClearCheck: function(){
		if(simData.getNazoClearFlag()){
			// 既にクリアしていたらチェックしない
			return;
		}

		var nazoCond = simView.getNazoCond();
		var list = NAZO_COND_LIST[nazoCond];
		var condPuyo = (list.puyo) ? simView.getNazoCondPuyo() : 0;
		var condNum = (list.num) ? simView.getNazoCondNum() : 0;

		var nazoClear = false;
		switch(nazoCond){
			case NAZO_COND_P_ALL:
				// 【ぷよ】全て消す
				nazoClear = simData.checkFieldAllEraseForNazo(condPuyo);
				break;
			case NAZO_COND_N_EQ_COLOR:
				// 【n】色消す
				nazoClear = (simData.getTotalEraseColor() == condNum);
				break;
			case NAZO_COND_N_GE_COLOR:
				// 【n】色以上消す
				nazoClear = (simData.getTotalEraseColor() >= condNum);
				break;
			case NAZO_COND_P_N_EQ_NUM:
				// 【ぷよ】【n】個消す
				nazoClear = (simData.getTotalErasePuyo(condPuyo) == condNum);
				break;
			case NAZO_COND_P_N_GE_NUM:
				// 【ぷよ】【n】個以上消す
				nazoClear = (simData.getTotalErasePuyo(condPuyo) >= condNum);
				break;
			case NAZO_COND_N_EQ_RENSA:
				// 【n】連鎖する
				nazoClear = (simData.getNowRensa() == condNum);
				break;
			case NAZO_COND_N_GE_RENSA:
				// 【n】連鎖以上する
				nazoClear = (simData.getNowRensa() >= condNum);
				break;
			case NAZO_COND_N_EQ_RENSA_P_ALL:
				// 【n】連鎖＆【ぷよ】全て消す
				nazoClear = ((simData.getNowRensa() == condNum) && simData.checkFieldAllEraseForNazo(condPuyo));
				break;
			case NAZO_COND_N_GE_RENSA_P_ALL:
				// 【n】連鎖以上＆【ぷよ】全て消す
				nazoClear = ((simData.getNowRensa() >= condNum) && simData.checkFieldAllEraseForNazo(condPuyo));
				break;
			case NAZO_COND_SAME_N_EQ_COLOR:
				// 【n】色同時に消す
				nazoClear = (simData.getNowEraseColor() == condNum);
				break;
			case NAZO_COND_SAME_N_GE_COLOR:
				// 【n】色以上同時に消す
				nazoClear = (simData.getNowEraseColor() >= condNum);
				break;
			case NAZO_COND_SAME_P_N_EQ_NUM:
				// 【ぷよ】【n】個同時に消す
				nazoClear = (simData.getNowErasePuyo(condPuyo) == condNum);
				break;
			case NAZO_COND_SAME_P_N_GE_NUM:
				// 【ぷよ】【n】個以上同時に消す
				nazoClear = (simData.getNowErasePuyo(condPuyo) >= condNum);
				break;
			case NAZO_COND_SAME_P_N_EQ_POS:
				// 【ぷよ】【n】組同時に消す
				nazoClear = (simData.getNowEraseGroup(condPuyo) == condNum);
				break;
			case NAZO_COND_SAME_P_N_GE_POS:
				// 【ぷよ】【n】組以上同時に消す
				nazoClear = (simData.getNowEraseGroup(condPuyo) >= condNum);
				break;
		}
		if(nazoClear){
			simData.setNazoClearFlag(true);
			simView.setNazoClearDisp(true);
		}
	},

	_dummy: function(){}
};


/**
 * 関数定義 - とこぷよ
 */
var simToko = {
	/**
	 * 出現色リストを自動的に生成する
	 */
	_makeColorHistoryAuto: function(){
		// 出現するぷよの色を取得
		var appearList = [];
		for(var i = TYPE_PUYO_BEGIN; i <= TYPE_PUYO_END; i++){
			if(simData.getAppearPuyoToko(i)){
				appearList.push(i);
			}
		}
		var appearListLen = appearList.length;

		// 全ツモパターンが1回ずつ出るリストを生成
		var appearPair = [];
		for(var i = 0; i < appearListLen; i++){
			for(var j = 0; j < appearListLen; j++){
				appearPair.push([appearList[i], appearList[j]]);
			}
		}
		var appearPairLen = appearPair.length;

		// 出現色リストをランダムに生成
		var colorHistory = [];
		var colorControl = simView.getColorControl();
		for(var i = 0, len = (COLOR_HISTORY_MAX / colorControl); i < len; i++){
			var indexList = [];
			for(var j = 0; j < colorControl; j++){
				indexList[j] = j % appearPairLen;
			}
			while(indexList.length > 0){
				colorHistory.push(appearPair[indexList.splice(getRandom(0, indexList.length - 1), 1)[0]]);
			}
		}

		// 初手制限による上書き
		switch(simView.getOpeningControl()){
			case 1:
				// 通1（初手2手が3色以内）
				if(appearListLen >= 4){
					// 出現するぷよの色が4色以上のときのみ処理
					for(i = 0; i < 2; i++){
						colorHistory[i][0] = appearList[getRandom(0, 2)];
						colorHistory[i][1] = appearList[getRandom(0, 2)];
					}
				}
				break;
			case 2:
				// 通2（初手3手が3色以内）
				if(appearListLen >= 4){
					// 出現するぷよの色が4色以上のときのみ処理
					for(i = 0; i < 3; i++){
						colorHistory[i][0] = appearList[getRandom(0, 2)];
						colorHistory[i][1] = appearList[getRandom(0, 2)];
					}
				}
				break;
			case 3:
				// フィーバー1（初手2手が3色、最初の3色は固定）
				if(appearListLen >= 3){
					// 出現するぷよの色が3色以上のときのみ処理
					colorHistory[0][0] = appearList[0];
					colorHistory[0][1] = appearList[1];
					colorHistory[1][0] = appearList[2];
					colorHistory[1][1] = appearList[getRandom(0, 2)];
				}
				break;
			case 4:
				// フィーバー2（初手2手が2～3色）
				if(appearListLen >= 3){
					// 出現するぷよの色が3色以上のときのみ処理
					var tempColorList = [];
					while(1){
						for(i = 0; i < 4; i++){
							tempColorList[i] = appearList[getRandom(0, 2)];
						}
						if((tempColorList[0] != tempColorList[1]) || (tempColorList[0] != tempColorList[2]) || (tempColorList[0] != tempColorList[3])){
							break;
						}
					}
					colorHistory[0][0] = tempColorList[0];
					colorHistory[0][1] = tempColorList[1];
					colorHistory[1][0] = tempColorList[2];
					colorHistory[1][1] = tempColorList[3];
				}
				break;
		}

		// 正式な出現色リストにデータを追加
		simData.initColorHistory();
		for(var i = 0, len = colorHistory.length; i < len; i++){
			simData.pushColorHistory(colorHistory[i]);
		}
	},

	/**
	 * 出現色リストをネクスト設定ボックスから生成する
	 */
	_makeColorHistoryByEdit: function(){
		// 出現色リストの初期化
		simData.initColorHistory();

		for(var i = 0; i < NEXT_SETTING_NUM; i++){
			var pair = [];
			switch(simData.getNextSettingType(i)){
				case HAND_TYPE_2:
					// 2個ぷよ
					pair = [simData.getNextSettingData(i, 2), simData.getNextSettingData(i, 0)];
					break;
				case HAND_TYPE_3A:
					// 3個ぷよ縦型
					pair = [simData.getNextSettingData(i, 2), simData.getNextSettingData(i, 3)];
					break;
				case HAND_TYPE_3B:
					// 3個ぷよ横型
					pair = [simData.getNextSettingData(i, 2), simData.getNextSettingData(i, 0)];
					break;
				case HAND_TYPE_4A:
					// 4個ぷよ2色
					pair = [simData.getNextSettingData(i, 2), simData.getNextSettingData(i, 3)];
					break;
				case HAND_TYPE_4B:
					// でかぷよ
					pair = [simData.getNextSettingData(i, 2), simData.getNextSettingData(i, 2)];
					break;
				case HAND_TYPE_OJ:
					// おじゃまぷよ
					pair = [TYPE_OJAMA, TYPE_OJAMA];
					break;
			}
			if((pair[0] == TYPE_NONE) || (pair[1] == TYPE_NONE)){
				// 未設定のユニットがあれば終了
				break;
			}
			simData.pushColorHistory(pair);
		}
		// 最後に空データを入れておく
		for(var j = 0; j < NEXT_MAX + 1; j++){
			simData.pushColorHistory([TYPE_NONE, TYPE_NONE]);
		}
	},

	/**
	 * ネクストの出現パターンをネクスト設定ボックスから生成する
	 */
	_makeNextPatternByEdit: function(){
		var pattern = [];
		for(var i = 0; i < NEXT_SETTING_NUM; i++){
			pattern.push(simData.getNextSettingType(i));
		}
		simData.setNextPattern(pattern);
	},

	/**
	 * とこぷよデータを初期化する
	 */
	_initTokopuyoData: function(){
		simData.setColorHistoryIndex(0);

		/*if(simData.getTokoMode() == TOKO_MODE_NORMAL){
			// 普通にとこぷよ
			var pattern;
			if(simView.getRule() == 'tsu'){
				// 通
				pattern = PATTERN_LIST[0];
			} else {
				// フィーバー
				pattern = FEVER_CHARA_LIST[simView.getSeries()][simView.getCulcChara()]['pattern'];
			}
			simData.setNextPattern(pattern);
		} else {
			// ぷよ譜／なぞぷよ
			this._makeNextPatternByEdit();
		}*/

		//simData.setNextPatternIndex(((simView.getOpeningControl() == 5) && (simData.getTokoMode() == TOKO_MODE_NORMAL)) ? getRandom(0, PATTERN_LOOP_NUM - 1) : 0);	// 5:フィーバー・出現パターンの途中から
		for(var i = 0; i < NEXT_MAX; i++){
			simData.setNextOne(i, i);
		}
		simData.forwardNext();

		simData.initHandDropHistory();

		// とこぷよ表示の更新
		//simView.redrawHand();
		//simView.redrawNext();
		//simView.redrawFieldImg();
		
		// handData を更新
		simData.updateHandData();

	},

	/**
	 * とこぷよをスタートする
	 */
	startTokopuyo: function(){
		// 色ごとの出現フラグをとこぷよ用にコピーする
		simData.copyAppearPuyoForToko();

		// 出現色リストを生成する
		if(simData.getTokoMode() == TOKO_MODE_NORMAL){
			// 普通にとこぷよ
			//this._makeColorHistoryAuto();
		} else {
			// ぷよ譜／なぞぷよ
			this._makeColorHistoryByEdit();
		}

		// とこぷよデータの初期化
		this._initTokopuyoData();

		// スコアをリセットする
		//simView.resetScoreText();

		// フィールドデータを保存する
		simData.buckupFieldData();
	},

	/**
	 * とこぷよをリトライする
	 */
	retryTokopuyo: function(){
		simData.tokoLoad(0);
	},

	/**
	 * 現在手を移動させる
	 */
	move: function(dir){
		if(simData.getHandType() == HAND_TYPE_OJ){
			// おじゃまぷよは操作できない
			return;
		}
		if(simData.moveHand(dir)){
			simView.redrawHand();
			simView.redrawFieldImg();
		}
	},

	/**
	 * 現在手を回転させる
	 */
	rotate: function(dir){
		if(simData.getHandType() == HAND_TYPE_OJ){
			// おじゃまぷよは操作できない
			return;
		}
		if(simData.rotateHand(dir)){
			simView.redrawHand();
			simView.redrawFieldImg();
		}
	},

	/**
	 * 現在手を落下させる
	 */
	drop: function(){
		if(simData.getHandType() == HAND_TYPE_OJ){
			// おじゃまぷよの落下処理
			var ojama = simData.adjustOjama(simData.getHandOjama());
			simData.dropOjama(ojama);

			// 落下履歴を保存する
			var handCondition = simData.getHandCondition();
			handCondition.ojama = ojama;
			simData.setHandDropHistory(simData.getHandIndex(), handCondition);
		} else {
			// 現在手の落下処理
			if(!simData.dropHand()){
				return;
			}

			// 落下履歴を保存する
			simData.setHandDropHistory(simData.getHandIndex(), simData.getHandCondition());
		}

		// とこぷよデータを更新
		simData.forwardNext();

		// 落下後の共通処理
		this._afterDrop();
	},

	/**
	 * おじゃまぷよを落下させる
	 */
	dropOjama: function(ojama){
		// 数値調整
		ojama = simData.adjustOjama(ojama);

		// おじゃまぷよの落下処理
		simData.dropOjama(ojama);

		if(simData.getHandType() == HAND_TYPE_NONE){
			// とこぷよを開始していなければ履歴としない
			simView.redrawFieldImg();
			return;
		}

		// 落下履歴を保存する
		simData.setHandDropHistory(simData.getHandIndex(), {
				type: HAND_TYPE_OJ,
				axis: [HAND_START_X, HAND_START_Y],
				dir: [0, -1],
				puyo: [TYPE_OJAMA, TYPE_NONE, TYPE_NONE, TYPE_NONE],
				ojama: ojama
			});

		// 手数を1増やす
		simData.incHandIndex();

		// 落下後の共通処理
		this._afterDrop();
	},

	/**
	 * 現在手、おじゃまぷよの落下後の共通処理
	 */
	_afterDrop: function(){
		// 現在のスコアをリセット
		simData.resetNowScore();

		// とこぷよ表示の更新
		/*simView.setScoreText();
		simView.redrawHand();
		simView.redrawNext();
		simView.redrawFieldImg();*/

		// 連鎖チェック
		simRensa.rensaCheck();
	},

	/**
	 * ぷよ譜の生成を行う
	 */
	makePuyoHistory: function(){
		// ぷよ譜データを初期化
		simView.initPuyoHistory();

		if(simData.getTokoMode() == TOKO_MODE_NORMAL){
			// ネクストを取得しておく
			var nextHandCondition = [];
			var nextNum = simView.getNextNum();
			if(nextNum >= 1){
				// 1手先
				nextHandCondition.push(simData.getHandCondition());
			}
			if(nextNum >= 2){
				// 2手先
				nextHandCondition.push({
					type: simData.getNowHandType(),
					puyo: simData.getNextData(0)
				});
			}

			// ネクスト設定ボックスをクリア
			simView.clearNextSetting();
		}

		for(var i = 0, len = Math.min(simData.getHandIndex(), NEXT_SETTING_NUM); i < len; i++){
			// 手の落下履歴を取得
			var handCondition = simData.getHandDropHistory(i);

			// ぷよ譜データをセット
			simData.setPuyoHistory(i, handCondition);

			// ネクスト設定ボックスを更新
			simData.setNextSettingType(i, handCondition.type);
			simView.setBorderNextSetting(i);
			if(handCondition.type == HAND_TYPE_OJ){
				// おじゃまぷよ
				for(var j = 0; j < HAND_PUYO_NUM_MAX; j++){
					simData.setNextSettingData(i, j, handCondition.puyo[j]);
				}
				simData.setNextSettingOjamaRow(i, handCondition.ojama[0]);
				simData.setNextSettingOjamaPos(i, handCondition.ojama[1]);
				simView.setNextSettingOjamaRow(i, handCondition.ojama[0]);
				simView.bulkSetNextSettingOjamaPos(i, handCondition.ojama[1]);
				simView.setNextSettingImg(i, 0, TYPE_OJAMA);
				$('.next_setting_ojama_div' + i).show();
			} else {
				// 色ぷよ
				for(var j = 0; j < HAND_PUYO_NUM_MAX; j++){
					simData.setNextSettingData(i, j, handCondition.puyo[j]);
					simView.setNextSettingImg(i, j, handCondition.puyo[j]);
				}
			}
		}
		if(simData.getTokoMode() == TOKO_MODE_NORMAL){
			// ネクストに見えている範囲は出す。ループカウンタの i は継続して使う
			for(var iNext = 0; iNext < nextNum; iNext++){
				if(i >= NEXT_SETTING_NUM){
					break;
				}
				// ネクスト設定ボックスを更新
				simData.setNextSettingType(i, nextHandCondition[iNext].type);
				simView.setBorderNextSetting(i);
				// 色ぷよ
				for(var j = 0; j < HAND_PUYO_NUM_MAX; j++){
					simData.setNextSettingData(i, j, nextHandCondition[iNext].puyo[j]);
					simView.setNextSettingImg(i, j, nextHandCondition[iNext].puyo[j]);
				}
				i++;
			}
		}
		simView.showPuyoHistoryControl();

		// とこぷよデータを更新してとこぷよ開始状態にする
		simData.setTokoMode(TOKO_MODE_EDIT);
		simData.tokoLoad(0);
		simToko.startTokopuyo();
		simToko.redrawHandByPuyoHistory();
	},

	/**
	 * ぷよ譜1手戻る
	 */
	backPuyoHistory: function(){
		simView.enablePuyoHistoryForwardButton();
		simData.tokoBack();
		simData.decPuyoHistoryIndex();
		simToko.redrawHandByPuyoHistory();
	},

	/**
	 * ぷよ譜1手進む
	 */
	forwardPuyoHistory: function(){
		var handCondition = simData.forwardPuyoHistory();
		if(!handCondition){
			return;
		}

		if(simData.isFinishedPuyoHistory()){
			// ぷよ譜データが最後まで進んだら「1手進む」ボタンを無効化する
			simView.disablePuyoHistoryForwardButton();
		}

		simData.setHandByCondition(handCondition);
		simData.setUpdateAfterDropFlag(true);
		simToko.drop();
	},

	/**
	 * 現在手をぷよ譜データで更新して表示を更新する
	 */
	redrawHandByPuyoHistory: function(){
		simData.updateHandByPuyoHistory();
		simView.redrawHand();
		simView.redrawFieldImg();
	},

	_dummy: function(){}
};


/**
 * 関数定義 - URL
 */
var simUrl = {
	/**
	 * 冗長なURLを出力するか
	 */
	_isFcodeLengthy: function(){
		// フィールドに色ぷよ、おじゃまぷよ以外の要素があれば冗長URLにする
		for(var py = 0; py < FIELD_Y_MAX + 1; py++){
			for(var px = 1; px < FIELD_X_MAX + 1; px++){
				if(simData.getFieldDataByPos(px, py) >= TYPE_LENGTHY_FCODE){
					return true;
				}
			}
		}
		return false;
	},

	/**
	 * 冗長なフィールドコードを生成する
	 */
	_getLengthyFcode: function(){
		// フィールドタイプを列挙して先頭に '=' をつける
		var fcode = '';
		for(var py = 0; py < FIELD_Y_MAX + 1; py++){
			for(var px = 1; px < FIELD_X_MAX + 1; px++){
				var type = simData.getFieldDataByPos(px, py);
				if((fcode == '') && (type == TYPE_NONE)){
					// 先頭の '0' は除外する
					continue;
				}
				fcode += ENCODE_CHAR.charAt(type);
			}
		}
		fcode = '=' + fcode;
		return fcode;
	},

	/**
	 * 短縮フィールドコードを生成する
	 */
	_getShortFcode: function(){
		// 2文字を1文字に圧縮する
		var fcode = '';
		var typeList = [];
		for(var py = 0; py < FIELD_Y_MAX + 1; py++){
			for(var px = 1; px < FIELD_X_MAX + 1; px++){
				var type = simData.getFieldDataByPos(px, py);
				if((fcode == '') && (typeList.length == 0) && (type == TYPE_NONE)){
					// 先頭の '0' は除外する
					continue;
				}
				if((fcode == '') && (typeList.length == 0) && (px % 2 == 0)){
					// 1文字目が偶数列目の場合、その前の列に '0' があるものとして扱う
					fcode += ENCODE_CHAR.charAt(type);
				} else {
					// 2文字を1文字に圧縮してフィールドコードにする
					typeList.push(type);
					if(typeList.length == 2){
						fcode += ENCODE_CHAR.charAt((typeList[0] << 3) | typeList[1]);
						typeList = [];
					}
				}
			}
		}
		return fcode;
	},

	/**
	 * ネクスト設定のフィールドコードを生成する
	 */
	_getNextSettingFcode: function(){
		// 手タイプの変換テーブル
		var handConv = [];
		handConv[HAND_TYPE_2]  = 0;
		handConv[HAND_TYPE_3A] = 1;
		handConv[HAND_TYPE_3B] = 2;
		handConv[HAND_TYPE_4A] = 3;
		handConv[HAND_TYPE_4B] = 4;

		// ぷよタイプの変換テーブル
		var puyoConv = [];
		puyoConv[TYPE_AKA]      = 0;
		puyoConv[TYPE_MIDORI]   = 1;
		puyoConv[TYPE_AO]       = 2;
		puyoConv[TYPE_KI]       = 3;
		puyoConv[TYPE_MURASAKI] = 4;
		var puyoConvLen = 5;

		var puyoHistoryLen = simData.getPuyoHistoryLength();
		var nextSettingData = [];
		var oneData;
		for(var i = 0; i < NEXT_SETTING_NUM; i++){
			var handType = simData.getNextSettingType(i);
			if(handType == HAND_TYPE_OJ){
				// おじゃまぷよ
				// ※ぷよ譜の axis に対応するビット(12bit中の上位3bit)に7を入れる
				oneData = (7 << 9);
				var ojama = simData.adjustOjama([simData.getNextSettingOjamaRow(i), simData.getNextSettingOjamaPos(i)]);
				oneData |= (ojama[0] << 6) | ojama[1];
			} else {
				// 色ぷよ
				var pair = [TYPE_NONE, TYPE_NONE];
				switch(handType){
					case HAND_TYPE_2:
						// 2個ぷよ
						pair = [simData.getNextSettingData(i, 2), simData.getNextSettingData(i, 0)];
						break;
					case HAND_TYPE_3A:
						// 3個ぷよ縦型
						pair = [simData.getNextSettingData(i, 2), simData.getNextSettingData(i, 3)];
						break;
					case HAND_TYPE_3B:
						// 3個ぷよ横型
						pair = [simData.getNextSettingData(i, 2), simData.getNextSettingData(i, 0)];
						break;
					case HAND_TYPE_4A:
						// 4個ぷよ2色
						pair = [simData.getNextSettingData(i, 2), simData.getNextSettingData(i, 3)];
						break;
					case HAND_TYPE_4B:
						// でかぷよ
						pair = [simData.getNextSettingData(i, 2), simData.getNextSettingData(i, 2)];
						break;
				}
				if((pair[0] == TYPE_NONE) || (pair[1] == TYPE_NONE)){
					// 未設定のユニットがあれば終了
					break;
				}
				// 1手のデータを 0～124 (7bit)の数値で表す
				var settingNum = handConv[handType] * (puyoConvLen * puyoConvLen) + puyoConv[pair[0]] * puyoConvLen + puyoConv[pair[1]];
				oneData = settingNum;

				// ぷよ譜のデータ
				if(i < puyoHistoryLen){
					// ※「4個ぷよ2色」の axis[1] の情報は dir から補完する
					var handCondition = simData.getPuyoHistory(i);
					var axis = handCondition.axis[0];	// 1～6の数値
					var dir = 0;
					if((handCondition.dir[0] == 0) && (handCondition.dir[1] == -1)){
						dir = 0;
					} else if((handCondition.dir[0] == 1) && (handCondition.dir[1] == 0)){
						dir = 1;
					} else if((handCondition.dir[0] == 0) && (handCondition.dir[1] == 1)){
						dir = 2;
					} else if((handCondition.dir[0] == -1) && (handCondition.dir[1] == 0)){
						dir = 3;
					}
					// 1手のデータを5bitの数値で表す
					var historyNum = ((axis << 2) + dir);
					oneData |= (historyNum << 7);
				}
			}
			nextSettingData.push(oneData);
		}

		// ビットデータに変換
		var bitData = [];
		for(var i = 0, len = nextSettingData.length; i < len; i++){
			for(var bit = 0; bit < FCODE_NEXT_DATA_BIT; bit++){
				bitData.push((nextSettingData[i] >> bit) & 1);
			}
		}
		return '_' + this._convertBitDataToFcode(bitData);
	},

	/**
	 * なぞぷよクリア条件のフィールドコードを生成する
	 */
	_getNazoCondFcode: function(){
		// なぞぷよクリア条件を取得
		var nazoCond = simView.getNazoCond();
		if(nazoCond == NAZO_COND_NONE){
			return '';
		}

		var list = NAZO_COND_LIST[nazoCond];
		var condPuyo = (list.puyo) ? simView.getNazoCondPuyo() : 0;
		var condNum = (list.num) ? simView.getNazoCondNum() : 0;

		// ビットデータに変換
		var bitData = [];
		for(var bit = 0; bit < FCODE_NAZO_COND_BIT; bit++){
			bitData.push((nazoCond >> bit) & 1);
		}
		for(var bit = 0; bit < FCODE_NAZO_COND_PUYO_BIT; bit++){
			bitData.push((condPuyo >> bit) & 1);
		}
		for(var bit = 0; bit < FCODE_NAZO_COND_NUM_BIT; bit++){
			bitData.push((condNum >> bit) & 1);
		}
		return '-' + this._convertBitDataToFcode(bitData);
	},

	/**
	 * フィールド図に対応したURLを出力する
	 */
	exportUrl: function(){
		// フィールドコードを生成
		var fcode;
		if(this._isFcodeLengthy()){
			// 冗長なURL
			fcode = this._getLengthyFcode();
		} else {
			// 短縮URL
			fcode = this._getShortFcode();
		}

		if((simData.getDispMode() == MODE_TOKOPUYO) && (simData.getTokoMode() == TOKO_MODE_EDIT)){
			// ぷよ譜／なぞぷよ
			fcode += this._getNextSettingFcode();
			fcode += this._getNazoCondFcode();
		}

		// URLを出力
		var href = location.href;
		var slashCount = 0;
		for(var i = 0, len = href.length; i < len; i++){
			if(location.href.charAt(i) == '/'){
				slashCount++;
			}
		}
		if(slashCount == 3){
			href = href + '/';
		}
		var url = href.substring(0, href.lastIndexOf('/') + 1) + fcode;
		simView.setExportedUrl(url);
	},

	/**
	 * 短縮URLのフィールドコードを展開する
	 */
	decodeShortFcode: function(shortFcode){
		// 1文字を2文字に分解する
		var fcode = '';
		for(var i = 0, len = shortFcode.length; i < len; i++){
			var num = Math.max(ENCODE_CHAR.indexOf(shortFcode.charAt(i)), 0);
			fcode += num >> 3;
			fcode += num & 7;
		}
		return fcode;
	},

	/**
	 * インポート用フィールドコードからフィールド図を生成する
	 */
	loadField: function(){
		var defaultFcode = simView.getPuyopFcode();
		if(defaultFcode == ''){
			// インポート用フィールドコードがなければ処理しない
			return;
		}

		// フィールドコードになぞぷよクリア条件が含まれていれば切り出す
		var pos;
		var fcodeForNazoCond = '';
		if((pos = defaultFcode.indexOf('-')) >= 0){
			fcodeForNazoCond = defaultFcode.substring(pos + 1);
			defaultFcode = defaultFcode.substring(0, pos);
		}

		// フィールドコードにネクスト設定、ぷよ譜が含まれていれば切り出す
		var fcodeForNext = '';
		if((pos = defaultFcode.indexOf('_')) >= 0){
			// ネクスト設定＋ぷよ譜
			fcodeForNext = defaultFcode.substring(pos + 1);
			defaultFcode = defaultFcode.substring(0, pos);
		}

		// フィールドコードを取得
		var fcode;
		if(defaultFcode.charAt(0) == '='){
			// 冗長なURL：先頭の '=' を取り除く
			fcode = defaultFcode.substring(1);
		} else {
			// 短縮URL：フィールドコードを展開する
			fcode = simUrl.decodeShortFcode(defaultFcode);
		}

		// フィールドコードからフィールド図を生成
		simUrl.fcodeToField(fcode);

		if((fcodeForNazoCond.length > 0) || (fcodeForNext.length > 0)){
			// ネクスト設定のための初期化
			this._initForNextSetting();

			// なぞぷよクリア条件を生成
			if(fcodeForNazoCond.length > 0){
				simUrl.settingFcodeToNazoCond(fcodeForNazoCond);
			}

			// ネクスト設定、ぷよ譜を生成
			if(fcodeForNext.length > 0){
				// ネクスト設定＋ぷよ譜
				if(simUrl.settingFcodeToNext(fcodeForNext)){
					// ぷよ譜のデータがある場合
					simView.showPuyoHistoryControl();
					// とこぷよデータを更新してとこぷよ開始状態にする
					simToko.startTokopuyo();
					simToko.redrawHandByPuyoHistory();
				} else {
					// ぷよ譜のデータがない場合
					// ※ぷよ譜におじゃまぷよデータがセットされている場合があるので初期化する
					simData.initPuyoHistoryData();
				}
			}
		}
	},

	/**
	 * イノセン堂のフィールドコードからフィールド図を生成する
	 */
	importInosendo: function(){
		// コード変換テーブル
		var convertInosendo = [
			TYPE_NONE,
			TYPE_OJAMA,
			TYPE_KABE,
			TYPE_TETSU,
			TYPE_AKA,
			TYPE_AO,
			TYPE_KI,
			TYPE_MIDORI,
			TYPE_MURASAKI
		];

		// イノセン堂のフィールドコードを取得
		var inosendoFcode = simView.getInosendoFcode();

		// フィールドコードを変換
		var fcode = '';
		for(var i = 0, len = inosendoFcode.length; i < len; i++){
			fcode += convertInosendo[parseInt(inosendoFcode.charAt(i))];
		}

		// フィールドコードからフィールド図を生成
		simUrl.fcodeToField(fcode);
	},

	/**
	 * フィールドコードからフィールド図を生成する
	 */
	fcodeToField: function(fcode){
		var fcodeLenMax = FIELD_X_MAX * (FIELD_Y_MAX + 1);	// フィールドコードの最大長

		if(fcode.length < fcodeLenMax){
			// フィールドコードが最大長に満たない場合は頭を0詰めする
			fcode = Array(fcodeLenMax - fcode.length + 1).join('0') + fcode;
		} else if(fcode.length > fcodeLenMax){
			// フィールドコードが最大長を超えている場合は切り落とす
			fcode = fcode.substring(0, fcodeLenMax);
		}

		// フィールドコードをフィールド図に反映
		for(var i = 0, len = fcode.length; i < len; i++){
			var px = ((i + FIELD_X_MAX) % FIELD_X_MAX) + 1;
			var py = parseInt(i / FIELD_X_MAX);
			simData.setFieldDataByPos(px, py, parseInt(fcode.charAt(i)));
		}
		simView.redrawFieldImg();
	},

	/**
	 * ネクスト設定のための初期化
	 */
	_initForNextSetting: function(){
		// 「とこぷよ」の「ぷよ譜／なぞぷよ」を表示
		simView.dispTokopuyoEdit();

		// ぷよ譜データを初期化
		simView.initPuyoHistory();

		// ネクスト設定ボックスをクリア
		simView.clearNextSetting();
	},

	/**
	 * ネクスト設定の手の状態を取得する
	 */
	_getHandConditionsByNum: function(oneData){
		// 手タイプの変換テーブル
		var handConv = [HAND_TYPE_2, HAND_TYPE_3A, HAND_TYPE_3B, HAND_TYPE_4A, HAND_TYPE_4B];

		// ぷよタイプの変換テーブル
		var puyoConv = [TYPE_AKA, TYPE_MIDORI, TYPE_AO, TYPE_KI, TYPE_MURASAKI];
		var puyoConvLen = puyoConv.length;

		var settingNum = (oneData & 127);
		var historyNum = (oneData >> 7);

		var handType = handConv[parseInt(settingNum / (puyoConvLen * puyoConvLen))];
		var colors = [puyoConv[parseInt(settingNum / puyoConvLen) % puyoConvLen], puyoConv[settingNum % puyoConvLen]];

		var handPuyo = [TYPE_NONE, TYPE_NONE, TYPE_NONE, TYPE_NONE];
		switch(handType){
			case HAND_TYPE_2:
				// 2個ぷよ
				handPuyo = [colors[1], TYPE_NONE, colors[0], TYPE_NONE];
				break;
			case HAND_TYPE_3A:
				// 3個ぷよ縦型
				handPuyo = [colors[0], TYPE_NONE, colors[0], colors[1]];
				break;
			case HAND_TYPE_3B:
				// 3個ぷよ横型
				handPuyo = [colors[1], TYPE_NONE, colors[0], colors[0]];
				break;
			case HAND_TYPE_4A:
				// 4個ぷよ2色
				handPuyo = [colors[0], colors[1], colors[0], colors[1]];
				break;
			case HAND_TYPE_4B:
				// でかぷよ
				handPuyo = [colors[0], colors[0], colors[0], colors[0]];
				break;
		}

		var handAxis = [0, 0];
		var handDir = [0, 0];
		if(historyNum > 0){
			// ぷよ譜用のデータ
			handAxis = [(historyNum >> 2), HAND_START_Y];
			switch(historyNum & 3){
				case 0:
					handDir = [0, -1];
					break;
				case 1:
					handDir = [1, 0];
					if(handType == HAND_TYPE_4A){
						handAxis[1]--;
					}
					break;
				case 2:
					handDir = [0, 1];
					if(handType == HAND_TYPE_4A){
						handAxis[1]--;
					}
					break;
				case 3:
					handDir = [-1, 0];
					break;
			}
		}

		return {
			type: handType,
			axis: handAxis,
			dir: handDir,
			puyo: handPuyo,
			ojama: [0, 0]
		};
	},

	/**
	 * ネクスト設定ボックスの更新
	 */
	_setNextSetting: function(settingIndex, handCondition){
		simData.setNextSettingType(settingIndex, handCondition.type);
		simView.setBorderNextSetting(settingIndex);
		if(handCondition.type == HAND_TYPE_OJ){
			// おじゃまぷよ
			for(var j = 0; j < HAND_PUYO_NUM_MAX; j++){
				simData.setNextSettingData(settingIndex, j, handCondition.puyo[j]);
			}
			simData.setNextSettingOjamaRow(settingIndex, handCondition.ojama[0]);
			simData.setNextSettingOjamaPos(settingIndex, handCondition.ojama[1]);
			simView.setNextSettingOjamaRow(settingIndex, handCondition.ojama[0]);
			simView.bulkSetNextSettingOjamaPos(settingIndex, handCondition.ojama[1]);
			simView.setNextSettingImg(settingIndex, 0, TYPE_OJAMA);
			$('.next_setting_ojama_div' + settingIndex).show();
		} else {
			// 色ぷよ
			for(var j = 0; j < HAND_PUYO_NUM_MAX; j++){
				simData.setNextSettingData(settingIndex, j, handCondition.puyo[j]);
				simView.setNextSettingImg(settingIndex, j, handCondition.puyo[j]);
			}
		}
	},

	/**
	 * フィールドコードからネクスト設定＋ぷよ譜を生成する
	 */
	settingFcodeToNext: function(fcode){
		// ビットデータに変換
		var bitData = this._convertFcodeToBitData(fcode);
		var settingIndex = 0;
		var existPuyoHistory = false;
		while(1){
			var index;
			var oneData = 0;
			for(var bit = 0; bit < FCODE_NEXT_DATA_BIT; bit++){
				index = settingIndex * FCODE_NEXT_DATA_BIT + bit;
				if(index >= bitData.length){
					// ビットデータの最後まで処理したら終了
					return existPuyoHistory;
				}
				oneData += (bitData[index] << bit);
			}
			var handCondition;
			if((oneData >> 9) == 7){
				// おじゃまぷよ
				var ojama = [((oneData >> 6) & 7), (oneData & 63)];
				handCondition = {
					type: HAND_TYPE_OJ,
					axis: [HAND_START_X, HAND_START_Y],
					dir: [0, -1],
					puyo: [TYPE_OJAMA, TYPE_NONE, TYPE_NONE, TYPE_NONE],
					ojama: [((oneData >> 6) & 7), (oneData & 63)]
				};
				// ぷよ譜データをセット
				// ※ここではぷよ譜の有無が不明なので、データセットのみしておく
				simData.setPuyoHistory(settingIndex, handCondition);
			} else {
				// 色ぷよ
				handCondition = this._getHandConditionsByNum(oneData);
				if(handCondition.axis[0] != 0){
					// ぷよ譜データをセット
					simData.setPuyoHistory(settingIndex, handCondition);
					existPuyoHistory = true;
				}
			}
			// ネクスト設定ボックスを更新
			//this._setNextSetting(settingIndex, handCondition);
			settingIndex++;
		}
	},

	/**
	 * フィールドコードからなぞぷよクリア条件を生成する
	 */
	settingFcodeToNazoCond: function(fcode){
		// ビットデータに変換
		var bitData = this._convertFcodeToBitData(fcode);

		var index;
		var nazoCond = 0;
		for(var bit = 0; bit < FCODE_NAZO_COND_BIT; bit++){
			index = bit;
			nazoCond += (index < bitData.length) ? (bitData[index] << bit) : 0;
		}

		var condPuyo = 0;
		for(var bit = 0; bit < FCODE_NAZO_COND_PUYO_BIT; bit++){
			index = bit + FCODE_NAZO_COND_BIT;
			condPuyo += (index < bitData.length) ? (bitData[index] << bit) : 0;
		}

		var condNum = 0;
		for(var bit = 0; bit < FCODE_NAZO_COND_NUM_BIT; bit++){
			index = bit + FCODE_NAZO_COND_BIT + FCODE_NAZO_COND_PUYO_BIT;
			condNum += (index < bitData.length) ? (bitData[index] << bit) : 0;
		}

		// なぞぷよクリア条件を更新
		simView.setNazoDisabled(nazoCond);
		simView.setNazoCond(nazoCond);
		simView.setNazoCondPuyo(condPuyo);
		simView.setNazoCondNum(condNum);
		simView.updateNazoDispText();
	},

	/**
	 * ビットデータをフィールドコードに変換する
	 */
	_convertBitDataToFcode: function(bitData){
		var fcode = '';
		while(1){
			var index;
			var num = 0;
			for(var bit = 0; bit < FCODE_CHAR_BIT; bit++){
				index = fcode.length * FCODE_CHAR_BIT + bit;
				num += (index < bitData.length) ? (bitData[index] << bit) : 0;
			}
			fcode += ENCODE_CHAR.charAt(num);
			if(index >= bitData.length - 1){
				break;
			}
		}
		return fcode;
	},

	/**
	 * フィールドコードをビットデータに変換する
	 */
	_convertFcodeToBitData: function(fcode){
		var bitData = [];
		for(var i = 0, len = fcode.length; i < len; i++){
			var num = Math.max(ENCODE_CHAR.indexOf(fcode.charAt(i)), 0);
			for(var bit = 0; bit < FCODE_CHAR_BIT; bit++){
				bitData.push((num >> bit) & 1);
			}
		}
		return bitData;
	},

	_dummy: function(){}
};


// 初期化
var init = function () {
    // fieldData を初期化
    simData.initFieldData();
    // fieldData をセット
    for (var py = 0; py < FIELD_Y_MAX + 2; py++) {
        for (var px = 0; px < FIELD_X_MAX + 2; px++) {
            if (py == FIELD_Y_MAX + 1) {
                simData.setFieldDataByPos(px, py, TYPE_KABE);
            } else if ((py >= 1) && ((px == 0) || (px == FIELD_X_MAX + 1))) {
                simData.setFieldDataByPos(px, py, TYPE_KABE);
            }
        }
    }
    // handData を初期化
    simData.initHandData();
};


var puyocolor = new Array();
puyocolor[0] = "NONE";
puyocolor[1] = "RED";
puyocolor[2] = "GREEN";
puyocolor[3] = "BLUE";
puyocolor[4] = "YELLOW";
puyocolor[5] = "PURPLE";

var toRotate = function (dir) {
    if (dir[0] > 0) {
        return "R0";
    } else if (dir[0] < 0) {
        return "R180";
    } else if (dir[1] > 0) {
        return "R270";
    } else {
        return "R90";
    }
};

// 初期化
init();
    
//var url = "imga6s5I0A6Q6Aiu3K8QgQhEawhS2E";
//var name = "name1";

var url = process.argv[2];
var name = process.argv[3];

simUrl.settingFcodeToNext(url);

for (var i = 0; i < simData._puyoHistory.length - 1; i++) {
    var stack = new Array();
    for (var rank = 0; rank < 13; rank++) {
        for (var row = 0; row < 6; row++) {
            stack[rank * 6 + row] = puyocolor[simData._fieldData[(13 - rank) * 8 - 7 + row]];
        }
    }
    
    var box = {
        name: name,
        state: "DOWN",
        sequence: i,
        currentPuyo: {
            cluster: {
                first: puyocolor[simData._puyoHistory[i].puyo[0]],
                second: puyocolor[simData._puyoHistory[i].puyo[2]]
            },
            rotate: toRotate(simData._puyoHistory[i].dir),
            row: simData._puyoHistory[i].axis[0] - 1,
            rank: 10 + (simData._puyoHistory[i].dir[1] == -1 ? 1 : 0)
        },
        score: 0,
        fallSpeed: "FAST",
        ojmCount: 0,
        storageOjmCount: 0,
        fallCount: 0,
        effectCount: 0,
        chainCount: 0,
        ojmFall: false,
        nextPuyo: {
            first: puyocolor[simData._puyoHistory[i + 1].puyo[0]],
            second: puyocolor[simData._puyoHistory[i + 1].puyo[2]]
        },
        stack: stack,
        ojms: []
    };
        
    // JSON出力
    var jsonStr = JSON.stringify(box);
    console.log(jsonStr);
        
    simToko.forwardPuyoHistory();
}


/*simUrl.settingFcodeToNext("imga6s5I0A6Q6Aiu3K8QgQhEawhS2E");
simToko.forwardPuyoHistory();
var jsonStr = JSON.stringify(simData._fieldData);
console.log(jsonStr);*/


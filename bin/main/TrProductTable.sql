CREATE TABLE TR_PRODUCT (
PRODUCT_ID VARCHAR(16) NOT NULL PRIMARY KEY,
PRODUCT_NAME VARCHAR(128) NOT NULL,
PRODUCT_SELLING_PRICE INTEGER NOT NULL,
PRODUCT_CATEGORY_ID CHAR(1) NOT NULL,
PRODUCT_STOCK INTEGER NOT NULL,
PRODUCT_COMMENT VARCHAR(2048),
PRODUCT_PHOTO_FILE_NAME1 VARCHAR(256),
PRODUCT_PHOTO_FILE_NAME2 VARCHAR(256),
PRODUCT_PHOTO_FILE_NAME3 VARCHAR(256),
PRODUCT_SHOW_FLAG INTEGER NOT NULL,
INSERT_DATE TIMESTAMP NOT NULL,
INSERT_USER VARCHAR(64) NOT NULL,
UPDATE_DATE TIMESTAMP NOT NULL,
UPDATE_USER VARCHAR(64) NOT NULL,
DELETE_DATE TIMESTAMP,
DELETE_USER VARCHAR(64)
);



INSERT INTO TR_PRODUCT VALUES(
'sample-1',
'サザコーヒー 德川将軍珈琲 豆 200g',
1500,
'0',
20,
'江戸幕末のフランス風コーヒー。
インドネシア産スマトラコーヒー豆の深煎り。
ベルベッドのようなワインに似た、濃厚できめ細かな甘さを最後の一滴まで味わえます。
たっぷりのミルクとの相性も抜群で『将軍カフェオレ』としても大人気です。',
'sample-1.jpg',
'sample-1.PNG',
NULL,
1,
'2020-04-09 00:02:00',
'佐藤由佑',
'2020-04-09 00:02:00',
'佐藤由佑',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-2',
'アバンス 徳用 キリマンブレンド (豆) 500g',
800,
'0',
30,
'焙煎したての焙煎豆が入っているので、新鮮なコーヒーの味わいを堪能することができます。
キリマンジャロの芳醇な香りを楽しめるブレンドになっているので、コーヒー好きには堪らないコーヒー豆に仕上がっているのが特徴です。',
'sample-2.jpg',
'sample-2.PNG',
NULL,
1,
'2020-04-12 10:30:00',
'佐藤由佑',
'2020-04-12 10:30:00',
'佐藤由佑',
NULL,
NULL
);

INSERT INTO TR_PRODUCT VALUES(
'sample-3',
'香りの女王（フルーティさNo1） モカ イルガチェフ ナチュラル精製G1（エチオピア コチャレ農園） コーヒー豆 （中煎） 銀河コーヒー (150g 【粉】中挽き)',
1080,
'0',
10,
'生産国・地域：エチオピア、イルガチェフ地方、コチャレ地区小規模農園。モカの品質評価基準９段階の最高等級G1のコーヒー豆です。',
'sample-3.jpg',
'sample-3.PNG',
NULL,
1,
'2020-04-12 10:30:00',
'佐藤由佑',
'2020-04-12 10:30:00',
'佐藤由佑',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-4',
'天馬珈琲 オーガニックコーヒー(250g)',
1080,
'0',
15,
' コーヒー豆 使用 （ 豆のまま ）「 日本バリスタ選手権 優勝3回焙煎士が 焙煎 」「 無農薬 有機栽培 オーガニック 」「 有機 JAS 規格 コーヒー 」「 自家焙煎 珈琲豆 」 ',
'sample-4.jpg',
'sample-4.PNG',
NULL,
1,
'2020-04-12 10:30:00',
'佐藤由佑',
'2020-04-12 10:30:00',
'佐藤由佑',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-5',
'ヒルス コーヒー (粉) リッチブレンド AP 750g',
1200,
'0',
10,
'【コクと香り】やや深炒りの豊かなコクと芳ばしい香りが特徴です。食後のひと時、くつろぎの時間に、ぜひお試しください。【原材料】コーヒー豆(生豆生産国名:ベトナム・ブラジル他)',
'sample-5.jpg',
'sample-5.PNG',
NULL,
1,
'now()',
'佐藤由佑',
'now()',
'佐藤由佑',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-6',
'ヒルス コーヒー豆 (粉) ナイト カフェインレス・ブラジル100% 170g',
700,
'0',
20,
'アラビカ種コーヒー生豆を100%使用し、生豆にやさしい製法でコーヒーの味・香りを損なうことなく、カフェインを97%カットしました。夕食後や就寝前にコーヒーが飲みたいけどカフェインは控えたい、そんな方にお勧めの「夜にやさしくリラックスできる」コーヒーです。エチオピア生豆独特の華やかな香りとフルーティーな味覚が特徴です。',
'sample-6.jpg',
'sample-6.PNG',
NULL,
1,
'now()',
'佐藤由佑',
'now()',
'佐藤由佑',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-7',
'ヒルス コーヒー 豆(粉) ハーモニアス ハワイ コナ ブレンド AP 170g',
980,
'0',
30,
'ハワイの肥沃な火山性の土壌と気候に育まれたコナコーヒーをブレンド。ブレンドの核は、「ハワイコナ」と「レインフォレスト・アライアンスの認証コーヒー」です。',
'sample-7.jpg',
'sample-7.PNG',
NULL,
1,
'now()',
'佐藤由佑',
'now()',
'佐藤由佑',
NULL,
NULL
);

INSERT INTO TR_PRODUCT VALUES(
'sample-8',
'アートコーヒー オーガニックコーヒー 粉 350g',
1200,
'0',
0,
'有機コーヒー豆は、化学的に合成された肥料及び農薬の使用を避けることを基本とし、自然の恵みを生かして栽培された有機コーヒー生豆を有機JAS認証を受けた業者が加工したコーヒーです。',
'sample-8.jpg',
'sample-8.PNG',
NULL,
0,
'now()',
'佐藤由佑',
'now()',
'佐藤由佑',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-9',
'小川珈琲店 有機珈琲オリジナルブレンド 粉 170g',
780,
'0',
2,
'原材料:有機コーヒー豆(生豆生産国名:ペルー、メキシコ、ブラジル、コロンビア、エルサルバドル)',
'sample-9.jpg',
'sample-9.PNG',
NULL,
1,
'now()',
'佐藤由佑',
'now()',
'佐藤由佑',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-10',
'小川珈琲店 有機珈琲バードフレンドリーブレンド 粉 170g',
880,
'0',
30,
'原材料:有機コーヒー豆(生豆生産国名:グアテマラ、ペルー、メキシコ)',
'sample-10.jpg',
'sample-10.PNG',
NULL,
1,
'now()',
'佐藤由佑',
'now()',
'佐藤由佑',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-11',
'小川珈琲店 有機珈琲フェアトレードモカブレンド 粉 170g',
680,
'0',
20,
'原材料:有機コーヒー豆(生豆生産国名:エチオピア、グアテマラ)',
'sample-11.jpg',
'sample-11.PNG',
NULL,
1,
'now()',
'佐藤由佑',
'now()',
'佐藤由佑',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-12',
'エスプレッソマシン　ラ・チンバリー　M21JU-DT/1 (100)水道直結型',
800000,
'1',
3,
'メーカー	LA CIMBALI
機種	M21JU-DT/1(100)
給水方法	水道直結式
給水栓Ｇ1/2 排水孔(φ40mm以上)
タイプ	エスプレッソ＋蒸気＋熱湯
電源	単相100V　50/60Hz
定格電流	12A
消費電力	1.2ｋW
外形寸法	幅320×奥行510×高さ430㎜(突起物を含む奥行580mm)
ボイラータンク容量	2.5L
エスプレッソ抽出能力	160杯/h
30㏄/1杯で2杯取り時
熱湯取出能力	8L/h
コーヒー抽出温度	約85℃
熱湯温度	約90℃
昇温時間	約20分(水温により多少異なります。)
質量	35kg
タンパーサイズ	57㎜
付属品	エスプレッソ抽出容器(2人用)：1
エスプレッソ抽出容器(1人用)：1
エスプレッソフィルター(2人用)：1
エスプレッソフィルター(1人用)：1
粉計量スプーン：1
粉圧縮器：1
洗浄ブラシ(エスプレッソ抽出器洗浄用)：1
エスプレッソ抽出器洗浄キャップ：1
洗浄漂白剤「バブルクリーン240g」：1
取扱説明書：1',
'sample-12.jpg',
'sample-12.PNG',
NULL,
1,
'now()',
'佐藤由佑',
'now()',
'佐藤由佑',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-13',
'HARIO(ハリオ) V60ドリップケトル・ヴォーノ VKB-70HSV-18',
3260,
'1',
5,
'ガス火・IH対応 実用500ml シルバー 日本製',
'sample-13.jpg',
'sample-13.PNG',
NULL,
1,
'now()',
'佐藤由佑',
'now()',
'佐藤由佑',
NULL,
NULL
);



ALTER TABLE TR_PRODUCT
ALTER COLUMN PRODUCT_CATEGORY_ID TYPE INTEGER
USING product_category_id::integer USING product_category_id::integer
;



ALTER TABLE TR_PRODUCT
ADD FOREIGN KEY (PRODUCT_CATEGORY_ID)
REFERENCES MS_PRODUCT_CATEGORY_INVENTORY (PRODUCT_CATEGORY_ID)
ON UPDATE RESTRICT
ON DELETE RESTRICT
;





INSERT INTO TR_PRODUCT VALUES(
'sample-14',
' ',
,
'0',
30,
' ',
'sample-14.jpg',
'sample-14.PNG',
NULL,
1,
'now()',
'佐藤由佑',
'now()',
'佐藤由佑',
NULL,
NULL
);

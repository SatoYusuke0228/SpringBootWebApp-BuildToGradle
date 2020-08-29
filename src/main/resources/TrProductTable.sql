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
'�T�U�R�[�q�[ ���쏫�R���� �� 200g',
1500,
'0',
20,
'�]�˖����̃t�����X���R�[�q�[�B
�C���h�l�V�A�Y�X�}�g���R�[�q�[���̐[����B
�x���x�b�h�̂悤�ȃ��C���Ɏ����A�Z���ł��ߍׂ��ȊÂ����Ō�̈�H�܂Ŗ��킦�܂��B
�����Ղ�̃~���N�Ƃ̑��������Q�Łw���R�J�t�F�I���x�Ƃ��Ă���l�C�ł��B',
'sample-1.jpg',
'sample-1.PNG',
NULL,
1,
'2020-04-09 00:02:00',
'�����R�C',
'2020-04-09 00:02:00',
'�����R�C',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-2',
'�A�o���X ���p �L���}���u�����h (��) 500g',
800,
'0',
30,
'���������Ă��������������Ă���̂ŁA�V�N�ȃR�[�q�[�̖��킢�����\���邱�Ƃ��ł��܂��B
�L���}���W�����̖F���ȍ�����y���߂�u�����h�ɂȂ��Ă���̂ŁA�R�[�q�[�D���ɂ͊���Ȃ��R�[�q�[���Ɏd�オ���Ă���̂������ł��B',
'sample-2.jpg',
'sample-2.PNG',
NULL,
1,
'2020-04-12 10:30:00',
'�����R�C',
'2020-04-12 10:30:00',
'�����R�C',
NULL,
NULL
);

INSERT INTO TR_PRODUCT VALUES(
'sample-3',
'����̏����i�t���[�e�B��No1�j ���J �C���K�`�F�t �i�`����������G1�i�G�`�I�s�A �R�`�����_���j �R�[�q�[�� �i�����j ��̓R�[�q�[ (150g �y���z���҂�)',
1080,
'0',
10,
'���Y���E�n��F�G�`�I�s�A�A�C���K�`�F�t�n���A�R�`�����n�揬�K�͔_���B���J�̕i���]����X�i�K�̍ō�����G1�̃R�[�q�[���ł��B',
'sample-3.jpg',
'sample-3.PNG',
NULL,
1,
'2020-04-12 10:30:00',
'�����R�C',
'2020-04-12 10:30:00',
'�����R�C',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-4',
'�V�n���� �I�[�K�j�b�N�R�[�q�[(250g)',
1080,
'0',
15,
' �R�[�q�[�� �g�p �i ���̂܂� �j�u ���{�o���X�^�I�茠 �D��3�������m�� ���� �v�u ���_�� �L�@�͔| �I�[�K�j�b�N �v�u �L�@ JAS �K�i �R�[�q�[ �v�u �������� ���蓤 �v ',
'sample-4.jpg',
'sample-4.PNG',
NULL,
1,
'2020-04-12 10:30:00',
'�����R�C',
'2020-04-12 10:30:00',
'�����R�C',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-5',
'�q���X �R�[�q�[ (��) ���b�`�u�����h AP 750g',
1200,
'0',
10,
'�y�R�N�ƍ���z���[�u��̖L���ȃR�N�ƖF�΂������肪�����ł��B�H��̂ЂƎ��A���났�̎��ԂɁA���Ђ��������������B�y���ޗ��z�R�[�q�[��(�������Y����:�x�g�i���E�u���W����)',
'sample-5.jpg',
'sample-5.PNG',
NULL,
1,
'now()',
'�����R�C',
'now()',
'�����R�C',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-6',
'�q���X �R�[�q�[�� (��) �i�C�g �J�t�F�C�����X�E�u���W��100% 170g',
700,
'0',
20,
'�A���r�J��R�[�q�[������100%�g�p���A�����ɂ₳�������@�ŃR�[�q�[�̖��E����𑹂Ȃ����ƂȂ��A�J�t�F�C����97%�J�b�g���܂����B�[�H���A�Q�O�ɃR�[�q�[�����݂������ǃJ�t�F�C���͍T�������A����ȕ��ɂ����߂́u��ɂ₳���������b�N�X�ł���v�R�[�q�[�ł��B�G�`�I�s�A�����Ɠ��̉؂₩�ȍ���ƃt���[�e�B�[�Ȗ��o�������ł��B',
'sample-6.jpg',
'sample-6.PNG',
NULL,
1,
'now()',
'�����R�C',
'now()',
'�����R�C',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-7',
'�q���X �R�[�q�[ ��(��) �n�[���j�A�X �n���C �R�i �u�����h AP 170g',
980,
'0',
30,
'�n���C�̔엀�ȉΎR���̓y��ƋC��Ɉ�܂ꂽ�R�i�R�[�q�[���u�����h�B�u�����h�̊j�́A�u�n���C�R�i�v�Ɓu���C���t�H���X�g�E�A���C�A���X�̔F�؃R�[�q�[�v�ł��B',
'sample-7.jpg',
'sample-7.PNG',
NULL,
1,
'now()',
'�����R�C',
'now()',
'�����R�C',
NULL,
NULL
);

INSERT INTO TR_PRODUCT VALUES(
'sample-8',
'�A�[�g�R�[�q�[ �I�[�K�j�b�N�R�[�q�[ �� 350g',
1200,
'0',
0,
'�L�@�R�[�q�[���́A���w�I�ɍ������ꂽ�엿�y�є_��̎g�p������邱�Ƃ���{�Ƃ��A���R�̌b�݂𐶂����č͔|���ꂽ�L�@�R�[�q�[������L�@JAS�F�؂��󂯂��Ǝ҂����H�����R�[�q�[�ł��B',
'sample-8.jpg',
'sample-8.PNG',
NULL,
0,
'now()',
'�����R�C',
'now()',
'�����R�C',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-9',
'��������X �L�@����I���W�i���u�����h �� 170g',
780,
'0',
2,
'���ޗ�:�L�@�R�[�q�[��(�������Y����:�y���[�A���L�V�R�A�u���W���A�R�����r�A�A�G���T���o�h��)',
'sample-9.jpg',
'sample-9.PNG',
NULL,
1,
'now()',
'�����R�C',
'now()',
'�����R�C',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-10',
'��������X �L�@����o�[�h�t�����h���[�u�����h �� 170g',
880,
'0',
30,
'���ޗ�:�L�@�R�[�q�[��(�������Y����:�O�A�e�}���A�y���[�A���L�V�R)',
'sample-10.jpg',
'sample-10.PNG',
NULL,
1,
'now()',
'�����R�C',
'now()',
'�����R�C',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-11',
'��������X �L�@����t�F�A�g���[�h���J�u�����h �� 170g',
680,
'0',
20,
'���ޗ�:�L�@�R�[�q�[��(�������Y����:�G�`�I�s�A�A�O�A�e�}��)',
'sample-11.jpg',
'sample-11.PNG',
NULL,
1,
'now()',
'�����R�C',
'now()',
'�����R�C',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-12',
'�G�X�v���b�\�}�V���@���E�`���o���[�@M21JU-DT/1 (100)���������^',
800000,
'1',
3,
'���[�J�[	LA CIMBALI
�@��	M21JU-DT/1(100)
�������@	����������
�������f1/2 �r���E(��40mm�ȏ�)
�^�C�v	�G�X�v���b�\�{���C�{�M��
�d��	�P��100V�@50/60Hz
��i�d��	12A
����d��	1.2��W
�O�`���@	��320�~���s510�~����430�o(�ˋN�����܂މ��s580mm)
�{�C���[�^���N�e��	2.5L
�G�X�v���b�\���o�\��	160�t/h
30�t/1�t��2�t��莞
�M����o�\��	8L/h
�R�[�q�[���o���x	��85��
�M�����x	��90��
��������	��20��(�����ɂ�葽���قȂ�܂��B)
����	35kg
�^���p�[�T�C�Y	57�o
�t���i	�G�X�v���b�\���o�e��(2�l�p)�F1
�G�X�v���b�\���o�e��(1�l�p)�F1
�G�X�v���b�\�t�B���^�[(2�l�p)�F1
�G�X�v���b�\�t�B���^�[(1�l�p)�F1
���v�ʃX�v�[���F1
�����k��F1
���u���V(�G�X�v���b�\���o����p)�F1
�G�X�v���b�\���o����L���b�v�F1
���Y���܁u�o�u���N���[��240g�v�F1
�戵�������F1',
'sample-12.jpg',
'sample-12.PNG',
NULL,
1,
'now()',
'�����R�C',
'now()',
'�����R�C',
NULL,
NULL
);



INSERT INTO TR_PRODUCT VALUES(
'sample-13',
'HARIO(�n���I) V60�h���b�v�P�g���E���H�[�m VKB-70HSV-18',
3260,
'1',
5,
'�K�X�΁EIH�Ή� ���p500ml �V���o�[ ���{��',
'sample-13.jpg',
'sample-13.PNG',
NULL,
1,
'now()',
'�����R�C',
'now()',
'�����R�C',
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
'�����R�C',
'now()',
'�����R�C',
NULL,
NULL
);

--�ӪA���򥻸��
CREATE TABLE caregivers (
  caregiver_id      INT IDENTITY(1,1) PRIMARY KEY,        --�~�A��ID
  chineseName       NVARCHAR(100) NOT NULL,               --�~�A������W�r
  gender            BIT 	NOT NULL,                     --�ʧO
  phone             NVARCHAR(20) unique,                  --�q��
  email             NVARCHAR(100),						  --�q�l�H�c
  experience_years  INT,                                  --�A�Ȧ~��
  photo			    NVARCHAR(MAX)	                      --�ҥ��
  );
  

INSERT INTO caregivers (chineseName, gender, phone, email, experience_years, photo)
VALUES 
(N'������', 0, N'0912345678', N'mei.hua@example.com', 5, N'photo1.jpg'),

(N'���ӱj', 1, N'0922333444', N'zhi.qiang@example.com', 8, N'photo2.jpg'),

(N'���p��', 0, N'0933221100', N'xiao.fang@example.com', 3, N'photo3.jpg'),

(N'�i�j��', 1, N'0955667788', N'da.wei@example.com', 10, N'photo4.jpg'),

(N'�L�ɽ�', 0, N'0966889900', N'yu.lian@example.com', 2, N'photo5.jpg');



--('BasicCleaning', '�򥻨���M��'),
--('DailyCare', '�򥻤�`�Ӯ�'),
--('MealSupport', '���\��U'),
--('MobilityAssist', '��ʨ�U');






--照服員基本資料
CREATE TABLE caregivers (
  caregiver_id      INT IDENTITY(1,1) PRIMARY KEY,        --居服員ID
  chineseName       NVARCHAR(100) NOT NULL,               --居服員中文名字
  gender            BIT 	NOT NULL,                     --性別
  phone             NVARCHAR(20) unique,                  --電話
  email             NVARCHAR(100),						  --電子信箱
  experience_years  INT,                                  --服務年資
  photo			    NVARCHAR(MAX)	                      --證件照
  );
  

INSERT INTO caregivers (chineseName, gender, phone, email, experience_years, photo)
VALUES 
(N'陳美華', 0, N'0912345678', N'mei.hua@example.com', 5, N'photo1.jpg'),

(N'李志強', 1, N'0922333444', N'zhi.qiang@example.com', 8, N'photo2.jpg'),

(N'李小芳', 0, N'0933221100', N'xiao.fang@example.com', 3, N'photo3.jpg'),

(N'張大偉', 1, N'0955667788', N'da.wei@example.com', 10, N'photo4.jpg'),

(N'林玉蓮', 0, N'0966889900', N'yu.lian@example.com', 2, N'photo5.jpg');



--('BasicCleaning', '基本身體清潔'),
--('DailyCare', '基本日常照料'),
--('MealSupport', '用餐協助'),
--('MobilityAssist', '行動協助');






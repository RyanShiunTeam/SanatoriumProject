/*SELECT*/
--Member
SELECT * FROM BackendUser
SELECT * FROM PwdReset
--Caregiver
SELECT * FROM caregivers
--RoomType	
SELECT * FROM RoomType
--Activity
SELECT * FROM Activity
--Rehabus
SELECT * FROM rehabus
--Device
SELECT * FROM DeviceCategory
SELECT * FROM Device


---------------------------------------------------------------------------------------------------------------------------------
/*CREATE SQL*/
--Member
CREATE TABLE BackendUser (
    user_ID          INT             IDENTITY(1,1)   PRIMARY KEY,       -- 使用者唯一識別
    user_name        NVARCHAR(50)    NOT NULL UNIQUE,                   -- 員工姓名
    password        NVARCHAR(255)   NOT NULL,                           -- 密碼
    email           NVARCHAR(200)   NULL,                               -- Email
    role            NVARCHAR(50)    NOT NULL DEFAULT 'Admin',           -- 身分／角色 (權限設定)
    is_active        BIT             NOT NULL DEFAULT 1,                -- 帳號啟用狀態 (1=啟用, 0=停用)
    createdAt       DATE       NOT NULL DEFAULT SYSUTCDATETIME(),		-- 建檔時間
    updatedAt       DATE       NOT NULL DEFAULT SYSUTCDATETIME(),		-- 資料最後更新時間
);

CREATE TABLE PwdReset (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_ID INT NOT NULL,
    code CHAR(6) NOT NULL,
    expiresAt DATETIME2 NOT NULL,
    used BIT NOT NULL DEFAULT 0
);

--Caregiver
CREATE TABLE caregivers (
  caregiver_id      INT IDENTITY(1,1) PRIMARY KEY,        --居服員ID
  chineseName       NVARCHAR(100) NOT NULL,               --居服員中文名字
  gender            BIT 	NOT NULL,                     --性別
  phone             NVARCHAR(20) NOT NULL unique,         --電話
  email             NVARCHAR(100) NOT NULL,			      --電子信箱
  experience_years  INT NOT NULL,                         --服務年資
  photo			    NVARCHAR(MAX) 	                      --證件照
  );
 
--RoomType
CREATE TABLE RoomType (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) NOT NULL,
    price INT NOT NULL,
    capacity INT NOT NULL, -- 原本的 acapacity，型別設為 INT
    description NVARCHAR(500),
    special_features NVARCHAR(500),
    image_url NVARCHAR(255)
);
--Activity
CREATE TABLE Activity (
    id INT PRIMARY KEY IDENTITY(1,1),			       
    name NVARCHAR(100) NOT NULL,				       
    category NVARCHAR(50),                             
    limit INT,							               
    --participant INT DEFAULT 0,					       -- Delete
    date DATE NOT NULL,							       -- 活動日期
	time NVARCHAR(20),							       -- 活動時間
    location NVARCHAR(100),                            -- 上課地點
    instructor NVARCHAR(100),                          -- 授課老師
    status BIT DEFAULT 1,                              -- 狀態（如：1 = 可報名、0 = 已額滿）
    --registrationstart DATE,                            -- Delete
    --registrationend DATE,                              -- Delete
    description NVARCHAR(MAX)                          -- 活動說明
);
--Rehabus
CREATE TABLE rehabus (

bus_id               INT   IDENTITY(1,1)     PRIMARY KEY               NOT NULL,           --車輛流水號
car_dealership      NVARCHAR(100)                                     NOT NULL,           --車行
bus_brand           NVARCHAR(100)                                     NOT NULL,           --汽車廠牌
bus_model           NVARCHAR(100)                                     NOT NULL,           --型號
seat_capacity        INT                                               NOT NULL,           --一般座位
wheelchair_capacity  INT                                               NOT NULL,           --輪椅座位
license_plate        NVARCHAR(20)                                      NOT NULL  UNIQUE   --車牌號碼

);
--Device
CREATE TABLE DeviceCategory (
    id INT IDENTITY(1,1) PRIMARY KEY,  -- 輔具分類ID
    name NVARCHAR(255) NOT NULL,        -- 分類名稱 
    category_id INT                       -- 分類排序
);

CREATE TABLE Device (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,         --商品名稱 
    sku NVARCHAR(50),                   -- 分類排序　商品貨號
    unitPrice DECIMAL(10,2),             ---單價
    inventory INT,                        --庫存 
    description NVARCHAR(MAX),            --商品描述
    image NVARCHAR(255),                   --商品圖片
    isOnline BIT,                         --是否上架
    category_id INT,                      -- 外鍵參考分類
    CONSTRAINT fk_device_category FOREIGN KEY (category_id) REFERENCES DeviceCategory(id)
);

CREATE TABLE emp_log (
    id          BIGINT      IDENTITY(1,1) PRIMARY KEY,
    user_id     INT         NOT NULL,        -- 操作者
    action      VARCHAR(50) NOT NULL,        -- 動作代號
    target_id   INT         NULL,            -- 被操作物件，如員工ID
    created_at  DATETIME2   NOT NULL DEFAULT SYSUTCDATETIME()
);
SELECT * FROM emp_log;

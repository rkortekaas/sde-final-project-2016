CREATE TABLE "MeasureDefinition"(
  "idMeasureDef" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "measureName" VARCHAR(45),
  "measureType" VARCHAR(45)
);
INSERT INTO "MeasureDefinition"("idMeasureDef","measureName","measureType") VALUES(1, 'weight', 'double');
INSERT INTO "MeasureDefinition"("idMeasureDef","measureName","measureType") VALUES(2, 'sleeping hours', 'double');
INSERT INTO "MeasureDefinition"("idMeasureDef","measureName","measureType") VALUES(3, 'steps', 'integer');
INSERT INTO "MeasureDefinition"("idMeasureDef","measureName","measureType") VALUES(4, 'blood pressure', 'double');
INSERT INTO "MeasureDefinition"("idMeasureDef","measureName","measureType") VALUES(5, 'heart rate', 'integer');
INSERT INTO "MeasureDefinition"("idMeasureDef","measureName","measureType") VALUES(6, 'bmi', 'double');
INSERT INTO "MeasureDefinition"("idMeasureDef","measureName","measureType") VALUES(7, 'calory intake', NULL);
CREATE TABLE "Person"(
  "idPerson" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "name" VARCHAR(255),
  "lastname" VARCHAR(255),
  "birthdate" DATETIME,
  "email" TEXT,
  "username" VARCHAR(45)
);
INSERT INTO "Person"("idPerson","name","lastname","birthdate","email","username") VALUES(1, 'Sarmad', 'Norris', '1945-01-01', 'chuck.norris@gmail.com', 'ganjasmoker01');
CREATE TABLE "Goals"(
  "idGoal" INTEGER PRIMARY KEY NOT NULL,
  "idPerson" INTEGER NOT NULL,
  "name" VARCHAR(45),
  "type" VARCHAR(45),
  "description" TEXT(500),
  "deadline" DATE,
  CONSTRAINT "fk_Goals_Person1"
    FOREIGN KEY("idPerson")
    REFERENCES "Person"("idPerson")
);
CREATE INDEX "Goals.fk_Goals_Person1_idx" ON "Goals" ("idPerson");
CREATE TABLE "Activity"(
  "idActivity" INTEGER PRIMARY KEY NOT NULL,
  "idPerson" INTEGER NOT NULL,
  "name" VARCHAR(45),
  "Description" TEXT(500),
  "Date" DATE,
  CONSTRAINT "fk_Activity_Person1"
    FOREIGN KEY("idPerson")
    REFERENCES "Person"("idPerson")
);
CREATE INDEX "Activity.fk_Activity_Person1_idx" ON "Activity" ("idPerson");
CREATE TABLE "HealthMeasureHistory"(
  "idMeasureHistory" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "idPerson" INTEGER NOT NULL,
  "idMeasureDefinition" INTEGER NOT NULL,
  "value" TEXT,
  "timestamp" TIMESTAMP,
  CONSTRAINT "fk_HealthMeasureHistory_MeasureDefinition1"
    FOREIGN KEY("idMeasureDefinition")
    REFERENCES "MeasureDefinition"("idMeasureDef"),
  CONSTRAINT "fk_HealthMeasureHistory_Person1"
    FOREIGN KEY("idPerson")
    REFERENCES "Person"("idPerson")
);
CREATE INDEX "HealthMeasureHistory.fk_HealthMeasureHistory_MeasureDefinition1_idx" ON "HealthMeasureHistory" ("idMeasureDefinition");
CREATE INDEX "HealthMeasureHistory.fk_HealthMeasureHistory_Person1_idx" ON "HealthMeasureHistory" ("idPerson");
INSERT INTO "HealthMeasureHistory"("idMeasureHistory","idPerson","idMeasureDefinition","value","timestamp") VALUES(1, 1, 1, '83', '2012-12-28');
INSERT INTO "HealthMeasureHistory"("idMeasureHistory","idPerson","idMeasureDefinition","value","timestamp") VALUES(2, 1, 1, '80', '2013-02-27');
INSERT INTO "HealthMeasureHistory"("idMeasureHistory","idPerson","idMeasureDefinition","value","timestamp") VALUES(3, 1, 1, '75', '2013-06-30');
CREATE TABLE "MeasureDefaultRange"(
  "idRange" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "idMeasureDef" INTEGER,
  "rangeName" VARCHAR(45),
  "startValue" TEXT,
  "endValue" TEXT,
  "alarmLevel" INTEGER,
  CONSTRAINT "fk_MeasureDefaultRange_MeasureDefinition1"
    FOREIGN KEY("idMeasureDef")
    REFERENCES "MeasureDefinition"("idMeasureDef")
);
CREATE INDEX "MeasureDefaultRange.fk_MeasureDefaultRange_MeasureDefinition1_idx" ON "MeasureDefaultRange" ("idMeasureDef");
INSERT INTO "MeasureDefaultRange"("idRange","idMeasureDef","rangeName","startValue","endValue","alarmLevel") VALUES(1, 6, 'Overweight', '25.01', '30', 1);
INSERT INTO "MeasureDefaultRange"("idRange","idMeasureDef","rangeName","startValue","endValue","alarmLevel") VALUES(2, 6, 'Obesity', '30.01', NULL, 2);
INSERT INTO "MeasureDefaultRange"("idRange","idMeasureDef","rangeName","startValue","endValue","alarmLevel") VALUES(3, 6, 'Normal weight', '20.01', '25', 0);
INSERT INTO "MeasureDefaultRange"("idRange","idMeasureDef","rangeName","startValue","endValue","alarmLevel") VALUES(4, 6, 'Underweight', NULL, '20', 1);
CREATE TABLE "Nutrition"(
  "idNutrition" INTEGER PRIMARY KEY NOT NULL,
  "idPerson" INTEGER NOT NULL,
  "date" DATE,
  CONSTRAINT "fk_Nutrition_Person1"
    FOREIGN KEY("idPerson")
    REFERENCES "Person"("idPerson")
);
CREATE INDEX "Nutrition.fk_Nutrition_Person1_idx" ON "Nutrition" ("idPerson");
CREATE TABLE "LifeStatus"(
  "idMeasure" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "idPerson" INTEGER,
  "idMeasureDef" INTEGER,
  "value" TEXT,
  CONSTRAINT "fk_LifeStatus_Person1"
    FOREIGN KEY("idPerson")
    REFERENCES "Person"("idPerson"),
  CONSTRAINT "fk_LifeStatus_MeasureDefinition1"
    FOREIGN KEY("idMeasureDef")
    REFERENCES "MeasureDefinition"("idMeasureDef")
);
CREATE INDEX "LifeStatus.fk_LifeStatus_Person1_idx" ON "LifeStatus" ("idPerson");
CREATE INDEX "LifeStatus.fk_LifeStatus_MeasureDefinition1_idx" ON "LifeStatus" ("idMeasureDef");
INSERT INTO "LifeStatus"("idMeasure","idPerson","idMeasureDef","value") VALUES(1, 1, 1, '72.3');
CREATE TABLE "Diet"(
  "idDiet" INTEGER PRIMARY KEY NOT NULL,
  "idNutrition" INTEGER NOT NULL,
  "name" VARCHAR(45),
  "calories" INTEGER,
  "quantity" INTEGER,
  CONSTRAINT "fk_Food_NutritionIntake1"
    FOREIGN KEY("idNutrition")
    REFERENCES "Nutrition"("idNutrition")
);
CREATE INDEX "Diet.fk_Food_NutritionIntake1_idx" ON "Diet" ("idNutrition");
COMMIT;

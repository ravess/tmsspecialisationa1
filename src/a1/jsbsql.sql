-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: jsb
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_active` int NOT NULL,
  `groups` varchar(255) DEFAULT '.',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (6,'bob','$2a$10$o.RHEuK9LyeYRH8Zp1jMx.3Rh9LmD39FaDkMU6xf1XGEYPAdwVTqi','bob@bob2.com',1,'.Admin.Dev.ProjectLead.ProjectManager.'),(29,'dev1','$2a$10$2f5Toea7.OB5/zEpRrP7uehmPgXvucddRFNYdySkpuamM1z/7AFVm','dev@gmail.com',1,'.Dev.'),(44,'pl','$2a$10$yIZwq2QcuBhcFjzEthXyye59LdwiQft.iZSWamEcE495cggxu6TC2','pl@gmail.com',1,'.ProjectLead.'),(45,'pm','$2a$10$6H1ryE9zbdnPYDbgj5tUL.7rFohFzQ9o52WD2.dWlVz7OW.Se2.Ty','pm@gmail.com',1,'.ProjectManager.'),(71,'admin','$2a$10$Ew51cjz/DJIGxk0I1N5ZMeC0mFdFyZgFMesr.mMym1x47c5XHX636','test@test.com',1,'.Admin.');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `application` (
  `app_acronym` varchar(65) NOT NULL,
  `app_description` longtext NOT NULL,
  `app_rnumber` int unsigned NOT NULL,
  `app_start_date` varchar(65) DEFAULT NULL,
  `app_end_date` varchar(65) DEFAULT NULL,
  `app_permit_open` varchar(65) DEFAULT NULL,
  `app_permit_todolist` varchar(65) DEFAULT NULL,
  `app_permit_doing` varchar(65) DEFAULT NULL,
  `app_permit_done` varchar(65) DEFAULT NULL,
  `app_permit_create` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`app_acronym`),
  UNIQUE KEY `App_Acronym_UNIQUE` (`app_acronym`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application`
--

LOCK TABLES `application` WRITE;
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` VALUES ('abc','abc123',56,'2023-08-25','2023-09-20','Admin','Admin','Admin','Admin','Admin'),('zoo','import React, { useContext, useEffect } from \"react\";\nimport Page from \"./Page\";\nimport Axios from \"axios\";\nimport DispatchContext from \"../DispatchContext\";\nimport StateContext from \"../StateContext\";\nimport LoadingDotsIcon from \"./LoadingDotsIcon\";\nimport { checkForCookie } from \"./Permissions\";\n\nfunction Login()\n{\n  const appDispatch = useContext(DispatchContext);\n  const appState = useContext(StateContext);\n\n  //Login button event handler\n  async function handleSubmit(e)\n  {\n    e.preventDefault();\n\n    //Get Form Data\n    const formData = new FormData(e.target);\n    const data = {};\n    for (let [key, value] of formData.entries()) { data[key] = value; }\n\n    //Warn if either fields empty, else send query to backend.\n    if (!data.username || !data.password)\n    { appDispatch({ type: \"flashMessage\", value: \"Please enter a username and password.\" }); }\n    else\n    {\n      async function fetchResults()\n      {\n        try\n        {\n          const response = await Axios.post(\"/login\", { username: data.username, password: data.password }, { withCredentials: true });\n          // console.log(response);\n          appDispatch({ type: \"login\" });\n          appDispatch({ type: \"flashMessage\", value: \"You have successfully logged in.\" });\n          appDispatch({ type: \"showLoading\", value: true });\n        } catch (e)\n        {\n          if (e.response.status === 403)\n          {\n            appDispatch({ type: \"flashMessage\", value: \"Your account has been disabled. Please contact your System Administrator for information.\" });\n          }\n          else\n          {\n            appDispatch({ type: \"flashMessage\", value: \"Invalid username / password.\" });\n          }\n        }\n      }\n      fetchResults();\n    }\n  }\n\n  if (appState.isLoading)\n  {\n    return <LoadingDotsIcon />;\n  }\n\n  return (\n    <Page title=\"Login\" wide={true}>\n      <form onSubmit={handleSubmit}>\n        <div className=\"form-group\">\n          <label htmlFor=\"username-login\" className=\"text-muted mb-1\">\n            <small>Username</small>\n          </label>\n          <input\n            id=\"username-login\"\n            name=\"username\"\n            className=\"form-control\"\n            type=\"text\"\n            placeholder=\"Enter Username\"\n            autoComplete=\"off\"\n          />\n        </div>\n        <div className=\"form-group\">\n          <label htmlFor=\"password-login\" className=\"text-muted mb-1\">\n            <small>Password</small>\n          </label>\n          <input\n            id=\"password-login\"\n            name=\"password\"\n            className=\"form-control\"\n            type=\"password\"\n            placeholder=\"Enter password\"\n          />\n        </div>\n        <button type=\"submit\" className=\"py-3 mt-4 btn btn-lg btn-success btn-block\">\n          Logon\n        </button>\n      </form>\n    </Page>\n  );\n}\n\nexport default Login;\n',12,'2023-10-05','2023-10-20','ProjectManager','Dev','Dev','ProjectLead','ProjectLead');
/*!40000 ALTER TABLE `application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `groups` (
  `group_name` varchar(255) NOT NULL,
  UNIQUE KEY `groupName_UNIQUE` (`group_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES ('Admin'),('Dev'),('ProjectLead'),('ProjectManager');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plan`
--

DROP TABLE IF EXISTS `plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plan` (
  `plan_mvp_name` varchar(65) NOT NULL,
  `plan_start_date` varchar(65) DEFAULT NULL,
  `plan_end_date` varchar(65) DEFAULT NULL,
  `plan_app_acronym` varchar(65) NOT NULL,
  `plan_color` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`plan_mvp_name`,`plan_app_acronym`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plan`
--

LOCK TABLES `plan` WRITE;
/*!40000 ALTER TABLE `plan` DISABLE KEYS */;
INSERT INTO `plan` VALUES ('emergency fix 1','','','zoo','#3d94d6'),('release 1','','','zoo','#ad4be2'),('release 2','','','zoo','#ff2e82'),('sprint 1','2023-10-05','2023-10-21','abc','#f22121'),('sprint 1',NULL,NULL,'zoo','#ff6666'),('sprint 2','2023-10-05','2023-10-31','abc','#815cd6'),('sprint 2',NULL,NULL,'zoo','#04ff00'),('sprint 3','','','zoo','#ff0000');
/*!40000 ALTER TABLE `plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `task_id` varchar(65) NOT NULL,
  `task_name` varchar(65) NOT NULL,
  `task_description` longtext NOT NULL,
  `task_notes` longtext,
  `task_plan` varchar(65) DEFAULT NULL,
  `task_app_acronym` varchar(65) NOT NULL,
  `task_state` varchar(65) NOT NULL DEFAULT 'Open',
  `task_creator` varchar(65) NOT NULL,
  `task_owner` varchar(65) NOT NULL,
  `task_create_date` varchar(65) NOT NULL,
  PRIMARY KEY (`task_id`),
  UNIQUE KEY `Task_id_UNIQUE` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES ('abc_11','popcorn','eat popcorn','_____________________________________________________________________________\nPromoted by: bob\nPromoted on: 2023-10-05 14:48:04 SGT\nState: [DONE] >>> [CLOSED]\nNotes: asdsadad\n_____________________________________________________________________________\n_____________________________________________________________________________\nPromoted by: bob\nPromoted on: 2023-10-04 17:09:41 SGT\nState: [DOING] >>> [DONE]\n_____________________________________________________________________________\nDemoted by: bob\nDemoted on: 2023-10-04 17:09:39 SGT\nState: [DONE] >>> [DOING]\nPlan changed from [blueberries] to [none]\n________________________________________________________\nPromoted by:bob\nPromoted on:2023-10-03 13:16:18 SGT\nState:[DOING] >>> [DONE]\nPlan changed from [strawberries] to [ blueberries]\nNotes: hello\n_______________________________________________________________________\nyellow','none','abc','CLOSED','bob','bob','2023-07-31'),('strawberry shortcake_8','testing123','testingpjsdpoajsdas','________________________________________________________\nTask ID: strawberry shortcake_8\nCreated by: bob\nCreated on: 2023-10-03 14:46:28 SGT\nState: OPEN\nNotes: No creation notes entered.\n________________________________________________________\n','','strawberry shortcake','OPEN','bob','bob','2023-10-03 14:46:28 SGT'),('zoo_10','tiger','4 legged god','_____________________________________________________________________________\nTask ID: zoo_10\nCreated by: pl\nCreated on: 2023-10-05 09:45:52 SGT\nState: OPEN\nNotes: No creation notes entered.\n_____________________________________________________________________________\n','none','zoo','OPEN','pl','pl','2023-10-05 09:45:52 SGT'),('zoo_11','lion','4 legged with tail god','_____________________________________________________________________________\nTask ID: zoo_11\nCreated by: pl\nCreated on: 2023-10-05 09:46:26 SGT\nState: OPEN\nNotes: this is a sample code that i used to start up my project\n\nimport React, { useContext, useEffect } from \"react\";\nimport Page from \"./Page\";\nimport Axios from \"axios\";\nimport DispatchContext from \"../DispatchContext\";\nimport StateContext from \"../StateContext\";\nimport LoadingDotsIcon from \"./LoadingDotsIcon\";\nimport { checkForCookie } from \"./Permissions\";\n\nfunction Login()\n{\n  const appDispatch = useContext(DispatchContext);\n  const appState = useContext(StateContext);\n\n  //Login button event handler\n  async function handleSubmit(e)\n  {\n    e.preventDefault();\n\n    //Get Form Data\n    const formData = new FormData(e.target);\n    const data = {};\n    for (let [key, value] of formData.entries()) { data[key] = value; }\n\n    //Warn if either fields empty, else send query to backend.\n    if (!data.username || !data.password)\n    { appDispatch({ type: \"flashMessage\", value: \"Please enter a username and password.\" }); }\n    else\n    {\n      async function fetchResults()\n      {\n        try\n        {\n          const response = await Axios.post(\"/login\", { username: data.username, password: data.password }, { withCredentials: true });\n          // console.log(response);\n          appDispatch({ type: \"login\" });\n          appDispatch({ type: \"flashMessage\", value: \"You have successfully logged in.\" });\n          appDispatch({ type: \"showLoading\", value: true });\n        } catch (e)\n        {\n          if (e.response.status === 403)\n          {\n            appDispatch({ type: \"flashMessage\", value: \"Your account has been disabled. Please contact your System Administrator for information.\" });\n          }\n          else\n          {\n            appDispatch({ type: \"flashMessage\", value: \"Invalid username / password.\" });\n          }\n        }\n      }\n      fetchResults();\n    }\n  }\n\n  if (appState.isLoading)\n  {\n    return <LoadingDotsIcon />;\n  }\n\n  return (\n    <Page title=\"Login\" wide={true}>\n      <form onSubmit={handleSubmit}>\n        <div className=\"form-group\">\n          <label htmlFor=\"username-login\" className=\"text-muted mb-1\">\n            <small>Username</small>\n          </label>\n          <input\n            id=\"username-login\"\n            name=\"username\"\n            className=\"form-control\"\n            type=\"text\"\n            placeholder=\"Enter Username\"\n            autoComplete=\"off\"\n          />\n        </div>\n        <div className=\"form-group\">\n          <label htmlFor=\"password-login\" className=\"text-muted mb-1\">\n            <small>Password</small>\n          </label>\n          <input\n            id=\"password-login\"\n            name=\"password\"\n            className=\"form-control\"\n            type=\"password\"\n            placeholder=\"Enter password\"\n          />\n        </div>\n        <button type=\"submit\" className=\"py-3 mt-4 btn btn-lg btn-success btn-block\">\n          Logon\n        </button>\n      </form>\n    </Page>\n  );\n}\n\nexport default Login;\n\n_____________________________________________________________________________\n','none','zoo','OPEN','pl','pl','2023-10-05 09:46:26 SGT'),('zoo_9','Monkey','2 legged god','_____________________________________________________________________________\nTask ID: zoo_9\nCreated by: pl\nCreated on: 2023-10-05 09:45:43 SGT\nState: OPEN\nNotes: No creation notes entered.\n_____________________________________________________________________________\n','none','zoo','OPEN','pl','pl','2023-10-05 09:45:43 SGT');
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-05 15:08:53

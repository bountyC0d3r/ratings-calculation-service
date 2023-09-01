package com.topcoder.ratings.controller;

import java.sql.Connection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topcoder.ratings.database.DBHelper;
import com.topcoder.ratings.services.coders.CoderService;

@RestController
@RequestMapping(path = "v5/ratings/coders")
public class CoderController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  DBHelper dbHelper;
  
  Connection oltpConn;
  Connection dwConn;

  java.sql.Timestamp fStartTime = null;

  @PostMapping(path = "/loadToDW", produces = "application/json")
  public ResponseEntity<String> loadCoders(@RequestBody Map<String, Object> body) throws Exception {

    fStartTime = new java.sql.Timestamp(System.currentTimeMillis());

    CoderService coderService = new CoderService();

    try {
      oltpConn = dbHelper.getConnection("OLTP");
      dwConn = dbHelper.getConnection("DW");

      coderService.getLastUpdateTime(dwConn);
      coderService.loadState(dwConn, oltpConn);
      coderService.loadCountry(dwConn, oltpConn);
      coderService.loadCoder(dwConn, oltpConn);
      coderService.loadSkillType(dwConn, oltpConn);
      coderService.loadSkill(dwConn, oltpConn);
      coderService.loadCoderSkill(dwConn, oltpConn);
      coderService.loadRating(dwConn, oltpConn);
      coderService.loadPath(dwConn, oltpConn);
      coderService.loadImage(dwConn, oltpConn);
      coderService.loadCoderImageXref(dwConn, oltpConn);
      coderService.loadSchool(dwConn, oltpConn);
      coderService.loadCurrentSchool(dwConn, oltpConn);
      coderService.loadAchievements(dwConn, oltpConn);
      coderService.loadTeam(dwConn, oltpConn);
      coderService.loadTeamCoderXref(dwConn, oltpConn);
      coderService.loadEvent(dwConn, oltpConn);
      coderService.loadEventRegistration(dwConn, oltpConn);
      coderService.loadUserNotifications(dwConn, oltpConn);
      coderService.setLastUpdateTime(fStartTime, dwConn);

      return new ResponseEntity<String>("coders loaded successfully ", null, HttpStatus.OK);

    } catch (Exception e) {
      logger.error("failed to load coders");
      logger.error(e.getMessage());
      logger.error("", e);
      return new ResponseEntity<String>("failed to load coders ", null,
          HttpStatus.BAD_REQUEST);

    } finally {
      dbHelper.closeConnection(dwConn);
      dbHelper.closeConnection(dwConn);
    }
  }
}

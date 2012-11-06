/**
 * Javascript Communication lib to Timetable Server.
 *
 * HM München - Fakultät 7 - Webtechnik - SS2012
 * @author M. Biebl (biebl@hm.edu) / 26.05.2012
 */
var WTSP = WTSP || {}; //Define namespace

WTSP.wtcommunicator = (function ()
{

   return {
      /**
       * Get all Profs from Server.
       * @param callback is the function that is called when all profs have been fetched.
       */
      getProfs: function (callback)
      {
         //Real-Time Call of Profs
         $.getJSON("/rest/json/profList/get", function (data)
         {
            var profArray = [];

            //Run through all elements
            $.each(data, function (i, profObject)
            {
               profArray[i] = new WTprofessor(  profObject.name, 
                                                profObject.id, 
                                                profObject.vorname );
            });

            callback(profArray);

            //Exception Handling
         }).error(function (jqXHR, textStatus, errorThrown)
         {
            alert("Error in WTSP.communicator.getProfs() \nType: " + textStatus + "\n" + 
                "HTTP Response Text: " + jqXHR.responseText + "\n" + 
                "Thrown Error: " + errorThrown);
         });
      },

      /**
       * Get all persons from the server.
       * @param callback is the function that is called when all persons have been fetched.
       */
      getPersons: function (callback)
      {
         //Real-Time Call of Personen
         $.getJSON("/rest/json/person/get", function (data)
         {
            var personArray = [];

            //Run through all elements
            $.each(data, function (i, personObject)
            {
               personArray[i] = new WTperson(   personObject.id, 
                                                personObject.firstname, 
                                                personObject.lastname,
                                                personObject.email );
            });

            callback(personArray);

            //Exception Handling
         }).error(function (jqXHR, textStatus, errorThrown)
         {
            alert("Error in WTSP.communicator.getPersons() \nType: " + textStatus + "\n" + 
                "HTTP Response Text: " + jqXHR.responseText + "\n" + 
                "Thrown Error: " + errorThrown);
         });
      },

      /**
       * Get all rooms from the server.
       * @param callback is the function that is called when all rooms have been fetched.
       */
      getRooms: function (callback)
      {
         //Real-Time Profs
         $.getJSON("/rest/json/rooms/roomList/get",

         function (data)
         {
            var roomArray = []; //Ergebnisvariable

            //Run through all elements
            $.each(data, function (i, roomObject)
            {
               roomArray[i] = new WTroom( roomObject.name, 
                                          roomObject.id, 
                                          roomObject.size );
            });


            callback(roomArray);

            //Exception Handling
         }).error(function (jqXHR, textStatus, errorThrown)
         {
            alert("Error in WTSP.communicator.getRooms() \nType: " + textStatus + "\n" + 
                "HTTP Response Text: " + jqXHR.responseText + "\n" + 
                "Thrown Error: " + errorThrown);
         });

      },

      /**
       * Get all study groups from the server.
       * @param callback is the function that is called when all study groups have been fetched.
       */
      getStudyGroups: function (callback)
      {
         //Real-Time Profs
         $.getJSON("/rest/json/studygroup/studygrouplist/get", function (data)
         {
            var groupArray = [];

            //Run through all elements
            $.each(data, function (i, groupObject)
            {
               groupArray[i] = new WTGroup(     groupObject.id,             
                                                groupObject.semester, 
                                                groupObject.program, 
                                                groupObject.groupCode );
            });

            callback(groupArray);

            //Exception Handling
         }).error(function (jqXHR, textStatus, errorThrown)
         {
            alert("Error in WTSP.communicator.getStudyGroups() \nType: " + textStatus + "\n" + 
                "HTTP Response Text: " + jqXHR.responseText + "\n" + 
                "Thrown Error: " + errorThrown);
         });

      },

      /**
       * Get the timetable of a professor.
       * @param pProfObjekt ist das Professoren Objekt, für den der Stundenplan angefordert werden soll.
       * @param callback is the function that is called, when the timetable has been fetched.
       */
      getProfPlan: function (pProfObjekt, callback)
      {
         //Real-Time Call des Plans
         $.getJSON("/rest/json/plan/profplan/get?id=" + pProfObjekt.id,

         function (data)
         {
            var lectureArray = [];

            //Run through all elements
            $.each(data, function (i, lectureObject)
            {
               lectureArray[i] = new WTlecture( lectureObject.id, 
                                                lectureObject.content, 
                                                lectureObject.tag, 
                                                lectureObject.title, 
                                                lectureObject.zeit );
            });

            callback(lectureArray);

            //Exception Handling
         }).error(function (jqXHR, textStatus, errorThrown)
         {
            alert("Error in WTSP.communicator.getProfPlan() \nType: " + textStatus + "\n" + 
                "HTTP Response Text: " + jqXHR.responseText + "\n" + 
                "Thrown Error: " + errorThrown);
         });
      },

      /**
       * Get the timetable of a study group.
       * @param pGroupObjekt is the study group object where the timetable is required.
       * @param callback is the function that is called, when the timetable has been fetched.
       */
      getGroupPlan: function (pGroupObjekt, callback)
      {
         var groupID = pGroupObjekt.id;

         //Real-Time Call des Plans
         $.getJSON("/rest/json/plan/studygroupPlan/get?id=" + groupID, function (data)
         {
            var lectureArray = [];

            //Run through all elements
            $.each(data, function (i, lectureObject)
            {
               lectureArray[i] = new WTlecture( lectureObject.id, 
                                                lectureObject.content, 
                                                lectureObject.tag, 
                                                lectureObject.title, 
                                                lectureObject.zeit );
            });

            callback(lectureArray);

            //Exception Handling
         }).error(function (jqXHR, textStatus, errorThrown)
         {
            alert("Error in WTSP.communicator.getGroupPlan() \nType: " + textStatus + "\n" + 
                "HTTP Response Text: " + jqXHR.responseText + "\n" + 
                "Thrown Error: " + errorThrown);
         });
      },

      /**
       * Get the room timetable.
       * @param pRoomObjekt is the room object where the timetable is required.
       * @param callback is the function that is called, when the timetable has been feteched.
       */
      getRoomPlan: function (pRoomObjekt, callback)
      {
         var roomID = pRoomObjekt.id;

         //Real-Time Call of the timetable
         $.getJSON("/rest/json/plan/roomPlan/get?id=" + roomID, function (data)
         {
            var lectureArray = []; //Ergebnisvariable

            //Run through all elements
            $.each(data, function (i, lectureObject)
            {
               lectureArray[i] = new WTlecture( lectureObject.id, 
                                                lectureObject.content, 
                                                lectureObject.tag, 
                                                lectureObject.title, 
                                                lectureObject.zeit );
            });

            callback(lectureArray);

            //Exception Handling
         }).error(function (jqXHR, textStatus, errorThrown)
         {
            alert("Error in WTSP.communicator.getRoomPlan() \nType: " + textStatus + "\n" + 
                "HTTP Response Text: " + jqXHR.responseText + "\n" + 
                "Thrown Error: " + errorThrown);
         });
      },

      /**
       * Get the occupency for the lecutre.
       * @param pLecture is the lecture object where the occupency is required.
       * @param callback is the function that is called, when the occupency has been feteched.
       */
      getOccu: function (pLecture, callback)
      {
         var lectureID = pLecture.id;

         //Real-Time Call of Occupency
         $.getJSON("/rest/json/plan/detailedStundenPositions/get?id=" + lectureID, function (data)
         {
            var occuPlan = new WToccu(data);

            callback(occuPlan);

            //Exception Handling
         }).error(function (jqXHR, textStatus, errorThrown)
         {
            alert("Error in WTSP.communicator.getOccu() \nType: " + textStatus + "\n" + 
                "HTTP Response Text: " + jqXHR.responseText + "\n" + 
                "Thrown Error: " + errorThrown);
         });
      },

      /**
       * Send changed lectures to the server.
       * @param pLecture is the lecture object that should be sent to the server.
       */
      setLecture: function (pLecture)
      {
         var lectureID = pLecture.id;

         $.ajax(
         {
            type: "PUT",
            url: "/rest/json/plan/stundenPositions/put/" + lectureID,
            contentType: 'application/json',
            data: JSON.stringify(
            {
               "title": pLecture.title,
               "id": lectureID,
               "tag": pLecture.tag,
               "zeit": pLecture.zeit,
               "content": pLecture.content
            }),
            dataType: 'json'
         }).fail(function (jqXHR, textStatus, errorThrown)
         {
            alert("Error in WTSP.communicator.setLecture() \nType: " + textStatus + "\n" + 
                "HTTP Response Text: " + jqXHR.responseText + "\n" + 
                "Thrown Error: " + errorThrown);
         });
      }
   };

})();

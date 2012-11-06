/**
 * Javascript Modul with constructors for the basic objects.
 *
 * HM München - Fakultät 7 - Webtechnik - SS2012
 * @author M. Biebl (biebl@hm.edu) / 26.05.2012
 */

/**
 * Prof object.
 *
 * @param name is the name of the professor.
 * @param id ist the unique identifier of the professor.
 */
function WTprofessor(name, id, vorname)
{
   this.name = name;
   this.id = id;
   this.vorname = vorname;
};

/**
 * Person object.
 *
 * @param id ist the unique identifier of a person.
 * @param firstname is the first name of the person.
 * @param lastname is the last name of the person.
 * @param email is the eamil address of the person.
 */
function WTperson(id, firstname, lastname, email)
{
   this.id = id;
   this.firstname = firstname;
   this.lastname = lastname;
   this.email = email;
};

/**
 * Room object.
 *
 * @param name is the name of the room.
 * @param id ist the unique identifier of the room.
 * @param size is size of the room.
 */
function WTroom(name, id, size)
{
   this.name = name;
   this.id = id;
   this.size = size;
};

/**
 * Study group object.
 *
 * @param id ist the unique identifier of the study group.
 * @param semester is the semester of the study group.
 * @param program is the study subject of the group.
 * @param groupCode is the group code like (A, B, C...) of the group.
 */
function WTGroup(id, semester, program, groupCode)
{
   this.id = id;
   this.semester = semester;
   this.program = program;
   this.groupCode = groupCode;
};

/**
 * Lecture object.
 *
 * @param id ist the unique identifier of the lecture.
 * @param content is the room name of the lecture.
 * @param tag is the day when the lecture takes place.
 * @param title is the name of the lecture.
 * @param time is the hour when the lecutre is.
 */
function WTlecture(id, content, tag, title, zeit)
{
   this.id = id;
   this.content = content;
   this.tag = tag;
   this.title = title;
   this.zeit = zeit;
};

/**
 * Occupency Plan Object.
 *
 * This object expects an 2-Dim Array with 8 Arrays with 6 elements.
 * Note: The first array and each first element are dummy values.
 *       The array from 1 - 7 is the time slot and
 *       the elements 1 -5 are the days.
 * 
 * @param occup is the 2-Dim array.
 */
function WToccu(occup)
{
   this.occup = occup;
};

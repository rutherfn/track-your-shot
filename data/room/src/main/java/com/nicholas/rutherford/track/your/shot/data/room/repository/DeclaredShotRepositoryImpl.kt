package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nicholas.rutherford.track.your.shot.data.room.dao.DeclaredShotDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.toDeclaredShotEntity

// todo -> Unit tests this since now where grabbing natively in the app.
class DeclaredShotRepositoryImpl(private val declaredShotDao: DeclaredShotDao) : DeclaredShotRepository {

    override suspend fun createNewDeclaredShot(declaredShot: DeclaredShot) = declaredShotDao.insert(declaredShotEntity = declaredShot.toDeclaredShotEntity())

    override suspend fun createDeclaredShots(shotIdsToFilterOut: List<Int>) {
        val declaredShots = declaredShotDao.getAllDeclaredShots()

        if (declaredShots.isEmpty()) {
            declaredShotDao.insert(declaredShotEntities = getDeclaredShotsFromJson().sortedBy { it.title }.map { it.toDeclaredShotEntity() }.filter { !shotIdsToFilterOut.contains(it.id) })
        }
    }
    override suspend fun updateDeclaredShot(declaredShot: DeclaredShot) =
        declaredShotDao.update(declaredShotEntity = declaredShot.toDeclaredShotEntity())

    override suspend fun fetchAllDeclaredShots(): List<DeclaredShot> =
        declaredShotDao.getAllDeclaredShots().map { it.toDeclaredShot() }

    override suspend fun deleteAllDeclaredShots() = declaredShotDao.deleteAll()

    override suspend fun deleteShotById(id: Int) = declaredShotDao.deleteDeclaredShotById(id = id)

    override suspend fun fetchDeclaredShotFromId(id: Int): DeclaredShot? =
        declaredShotDao.getDeclaredShotFromId(id = id)?.toDeclaredShot()

    override suspend fun fetchDeclaredShotsBySearchQuery(searchQuery: String): List<DeclaredShot> =
        declaredShotDao.getDeclaredShotsBySearchQuery(searchQuery = searchQuery).map { it.toDeclaredShot() }

    fun getDeclaredShotsFromJson(): List<DeclaredShot> {
        val json = """
[
  {
    "id": 1,
    "shotCategory": "inside",
    "title": "Layup",
    "description": "A layup is a fundamental and common inside shot where a player drives towards the basket and releases the ball near the hoop with one hand."
  },
  {
    "id": 2,
    "shotCategory": "inside",
    "title": "Finger Roll",
    "description": "Similar to a layup, the finger roll is a soft, high-arcing shot where the player uses their fingertips to release the ball gently into the basket."
  },
  {
    "id": 3,
    "shotCategory": "inside",
    "title": "Hook Shot",
    "description": "A hook shot involves a one-handed shot with a sweeping, hooking motion of the arm. It is often used by post players close to the basket."
  },
  {
    "id": 4,
    "shotCategory": "inside",
    "title": "Jump Hook",
    "description": "Similar to a hook shot, the jump hook involves a player jumping while executing the hook shot. This adds elevation to the shot and helps avoid defenders."
  },
  {
    "id": 5,
    "shotCategory": "inside",
    "title": "Baby Hook",
    "description": "A shorter version of the hook shot, typically used when a player is very close to the basket."
  },
  {
    "id": 6,
    "shotCategory": "inside",
    "title": "Drop Step",
    "description": "A post move where a player, after receiving the ball in the post, makes a quick step towards the baseline to create separation from the defender and takes a close-range shot."
  },
  {
    "id": 7,
    "shotCategory": "mid-range",
    "title": "Fadeaway",
    "description": "A fadeaway is a jump shot taken while leaning backward, away from the defender, making it more difficult to block."
  },
  {
    "id": 8,
    "shotCategory": "mid-range",
    "title": "Pull-Up Jumper",
    "description": "A pull-up jumper is taken after a quick stop and jump, usually from mid-range, without going all the way to the hoop."
  },
  {
    "id": 9,
    "shotCategory": "mid-range",
    "title": "Step-Back Jumper",
    "description": "The step-back jumper is executed by a player stepping back to create space from a defender before taking a shot."
  },
  {
    "id": 10,
    "shotCategory": "mid-range",
    "title": "Bank Shot",
    "description": "A bank shot is a shot that hits the backboard first before going into the basket, often used from mid-range distances."
  },
  {
    "id": 11,
    "shotCategory": "mid-range / long-range",
    "title": "Catch and Shoot",
    "description": "A quick shot taken immediately after catching the ball, usually from mid-range or the three-point line."
  },
  {
    "id": 12,
    "shotCategory": "long-range",
    "title": "Three-Point Shot",
    "description": "A three-point shot is a shot made from beyond the three-point line, scoring three points if successful."
  },
  {
    "id": 13,
    "shotCategory": "long-range",
    "title": "Step-Back Three",
    "description": "A step-back three is a three-point shot taken by stepping back to create space from a defender."
  },
  {
    "id": 14,
    "shotCategory": "long-range",
    "title": "Deep Three",
    "description": "A deep three is an exceptionally long three-point shot taken well beyond the three-point line."
  },
  {
    "id": 15,
    "shotCategory": "long-range",
    "title": "Catch and Shoot Three",
    "description": "A three-point shot taken immediately after catching a pass, usually from a spot-up position."
  },
  {
    "id": 16,
    "shotCategory": "long-range",
    "title": "Off-The-Dribble Three",
    "description": "A three-point shot taken after a player dribbles to create their shot opportunity."
  },
  {
    "id": 17,
    "shotCategory": "free throw",
    "title": "Standard Free Throw",
    "description": "A free throw is an uncontested shot from the free-throw line, taken after a foul is committed."
  },
  {
    "id": 20,
    "shotCategory": "transition",
    "title": "Fast Break Layup",
    "description": "A layup made during a fast break situation, typically when the defense is caught off guard."
  },
  {
    "id": 21,
    "shotCategory": "transition",
    "title": "Eurostep",
    "description": "The Eurostep is a move involving two quick steps in different directions to evade a defender on a drive to the hoop."
  },
  {
    "id": 22,
    "shotCategory": "transition",
    "title": "Coast-to-Coast Layup",
    "description": "A layup scored by a player driving the length of the court without passing."
  },
  {
    "id": 23,
    "shotCategory": "post-up",
    "title": "Post-Up Turnaround",
    "description": "A shot taken by a player in the post after backing their defender down and turning to shoot."
  },
  {
    "id": 24,
    "shotCategory": "post-up",
    "title": "Face-Up Jumper",
    "description": "A mid-range shot taken by a post player facing their defender instead of backing them down."
  },
  {
    "id": 25,
    "shotCategory": "post-up",
    "title": "Drop Step Layup",
    "description": "A layup from a quick drop step move in the post to evade the defender."
  },
  {
    "id": 30,
    "shotCategory": "tip-ins",
    "title": "Tip-In",
    "description": "A shot made by tapping the ball into the basket, usually off a missed shot."
  },
  {
    "id": 33,
    "shotCategory": "transition",
    "title": "Trail Three Pointer",
    "description": "A three-point shot taken by a trailing player during a fast-break opportunity."
  },
  {
    "id": 34,
    "shotCategory": "mid-range",
    "title": "Running Fadeaway",
    "description": "A fadeaway jumper while in motion toward the basket."
  },
  {
    "id": 35,
    "shotCategory": "inside",
    "title": "Layup with Contact",
    "description": "A layup taken while absorbing contact from a defender."
  },
  {
    "id": 36,
    "shotCategory": "step-back",
    "title": "Step-Back Reverse Hook",
    "description": "A reverse hook shot combined with a step-back motion to create separation."
  },
  {
    "id": 39,
    "shotCategory": "transition",
    "title": "Fast Break Three",
    "description": "A three-point shot taken during a fast break."
  },
  {
    "id": 40,
    "shotCategory": "mid-range",
    "title": "Pull-Up Bank Shot",
    "description": "A pull-up jumper off the backboard from mid-range distance."
  }
]
        """.trimIndent()

        val typeToken = object : TypeToken<List<DeclaredShot>>() {}.type
        return Gson().fromJson(json, typeToken)
    }
}

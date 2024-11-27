package com.nicholas.rutherford.track.your.shot.base.resources.declaredshotsjson

class FakeDeclaredShotsJson : DeclaredShotsJson {
    override fun fetchJsonDeclaredShots(): String {
        return """
  [
    {
      "id": 1,
      "shotCategory": "inside",
      "title": "Layup",
      "description": "A layup is a fundamental and common inside shot where a player drives towards the basket and releases the ball near the hoop with one hand."
    }
  ]
  """
    }

    override fun writeToJsonDeclaredShots() {
    }
}

package com.livefront.codechallenge

import com.livefront.codechallenge.data.Character
import com.livefront.codechallenge.data.Connections
import com.livefront.codechallenge.data.Images
import com.livefront.codechallenge.data.Work

internal val characters = listOf(
    Character(
        id = 1,
        name = "Superman",
        work = Work(
            occupation = "Reporter",
            base = "Metropolis"
        ),
        connections = Connections(
            groupAffiliation = "Justice League",
            relatives = "Lois Lane (Wife), Jor-El (Father), Lara (Mother)"
        ),
        images = Images(
            extraSmall = "",
            small = "",
            medium = "",
            large = ""
        )
    ),
    Character(
        id = 2,
        name = "Batman",
        work = Work(
            occupation = "Businessman",
            base = "Gotham City"
        ),
        connections = Connections(
            groupAffiliation = "Justice League",
            relatives = "Damian Wayne (Son), Dick Grayson (Adopted Son)"
        ),
        images = Images(
            extraSmall = "",
            small = "",
            medium = "",
            large = ""
        )
    )
)
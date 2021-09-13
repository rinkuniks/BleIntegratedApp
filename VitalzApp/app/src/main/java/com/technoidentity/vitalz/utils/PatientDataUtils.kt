package com.technoidentity.vitalz.utils


    fun checkHeartRateThreshold(pair: Pair<Int, Byte>): Boolean {

        return when {
            pair.first in (0..20) && pair.second !in (100..170) -> {
                return true
            }
            pair.first in (20..30) && pair.second !in (95..162) -> {
                return true
            }
            pair.first in (30..35) && pair.second !in (93..157) -> {
                return true
            }
            pair.first in (40..45) && pair.second !in (90..153) -> {
                return true
            }
            pair.first in (45..50) && pair.second !in (88..149) -> {
                return true
            }
            pair.first in (50..55) && pair.second !in (85..145) -> {
                return true
            }
            pair.first in (55..60) && pair.second !in (83..140) -> {
                return true
            }
            pair.first in (60..65) && pair.second !in (80..136) -> {
                return true
            }
            pair.first in (65..70) && pair.second !in (78..132) -> {
                return true
            }
            else -> false
        }
    }



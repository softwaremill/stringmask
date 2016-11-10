package com.softwaremill.macros.customize

import org.scalatest.{FlatSpec, Matchers}

class ToStringMaskOnBigClassesTest extends FlatSpec with Matchers {

  behavior of "masking"

  it should "work on classes with > 22 fields" in {
    // given
    val bc = BigClass(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 ,14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25)

    // then
    bc.toString should be("BigClass(***,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25)")
  }

  @customize
  case class BigClass(@mask a1: Int, a2: Int, a3: Int, a4: Int, a5: Int, a6: Int, a7: Int, a8: Int, a9: Int, a10: Int,
                      a11: Int, a12: Int, a13: Int, a14: Int,a15: Int, a16: Int, a17: Int, a18: Int, a19: Int, a20: Int,
                      a21: Int, a22: Int, a23: Int, a24: Int, a25: Int)

}

package org.codeoverflow.chatoverflow.requirement.parameter

import org.codeoverflow.chatoverflow.api.io.parameter.ColorParameter
import org.codeoverflow.chatoverflow.registry.Impl
import java.awt.Color

/**
  * A parameter holding a Color value.
  */
@Impl(impl = classOf[ColorParameter])
class ColorParameterImpl extends ColorParameter {
  private var value: Color = null

  override def getType: Class[Color] = classOf[Color]

  override def serialize(): String = s"${value.getRed},${value.getGreen},${value.getBlue},${value.getAlpha}"

  override def get(): Color = value

  override def deserialize(value: String): Unit = {

    val hex3 = "^#([a-fA-F0-9]{6})$".r
    val hex4 = "^#([a-fA-F0-9]{8})$".r
    val int3 = "^(\\d+),(\\d+),(\\d+)$".r
    val int4 = "^(\\d+),(\\d+),(\\d+),(\\d+)$".r
    val float3 = "^(\\d+(?:\\.\\d+)?),(\\d+(?:\\.\\d+)?),(\\d+(?:\\.\\d+)?)$".r
    val float4 = "^(\\d+(?:\\.\\d+)?),(\\d+(?:\\.\\d+)?),(\\d+(?:\\.\\d+)?),(\\d+(?:\\.\\d+)?)$".r

    try {
      value match {
        case hex3(hex) => {
          set(new Color(Integer.valueOf(hex.substring(0, 2), 16),
            Integer.valueOf(hex.substring(2, 4), 16),
            Integer.valueOf(hex.substring(4, 6), 16)))
        }
        case hex4(hex) =>
          set(new Color(Integer.valueOf(hex.substring(0, 2), 16),
            Integer.valueOf(hex.substring(2, 4), 16),
            Integer.valueOf(hex.substring(4, 6), 16),
            Integer.valueOf(hex.substring(6, 8), 16)))
        case int3(r, g, b) => set(new Color(r.toInt, g.toInt, b.toInt))
        case int4(r, g, b, a) => set(new Color(r.toInt, g.toInt, b.toInt, a.toInt))
        case float3(r, g, b) => set(new Color(r.toFloat, g.toFloat, b.toFloat))
        case float4(r, g, b, a) => set(new Color(r.toFloat, g.toFloat, b.toFloat, a.toFloat))
      }
    } catch {
      case _: Exception => print("Can't convert String to color")
    }
  }

  override def set(value: Color): Unit = this.value = value

}
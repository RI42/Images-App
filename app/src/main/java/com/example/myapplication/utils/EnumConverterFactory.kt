package com.example.myapplication.utils

//class EnumConverterFactory : Converter.Factory() {
//    override fun stringConverter(
//        type: Type,
//        annotations: Array<Annotation>,
//        retrofit: Retrofit
//    ): Converter<Enum<*>, String>? =
//        if (type is Class<*> && type.isEnum) {
//            Converter { enum ->
//                try {
//                    enum.javaClass.getField(enum.name)
//                        .getAnnotation(SerializedName::class.java)?.value
//                } catch (exception: Exception) {
//                    null
//                } ?: enum.toString()
//            }
//        } else {
//            null
//        }
//}

package com.example.campuspeer.model

enum class Category(val label: String) {
    ELECTRONICS("전자기기"),
    BOOKS("도서"),
    FASHION("패션"),
    BEAUTY("뷰티"),
    SPORTS("스포츠"),
    FURNITURE("가구"),
    ETC("기타");

    override fun toString(): String = label
}
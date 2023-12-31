package com.waminiyi.realestatemanager.firebase.model

/**
 * Represents a Firestore path, which can be either a collection path or a document path.
 */
sealed class FirestorePath {
    /**
     * Represents a collection path in Firestore.
     *
     * @param pathSegments The list of strings representing path segments composing the collection path.
     * @throws IllegalArgumentException if the collection path is empty or has an even number of segments.
     */
    data class CollectionPath(val pathSegments: List<String>) : FirestorePath() {
        init {
            require(pathSegments.isNotEmpty()) { "Collection path should not be empty" }
            require(pathSegments.size % 2 != 0) { "Invalid collection path size, should be odd" }
        }

        override fun toString(): String = pathSegments.joinToString("/")
    }

    /**
     * Represents a document path in Firestore.
     *
     * @param pathSegments The list of strings representing path segments composing the document path.
     * @throws IllegalArgumentException if the document path is empty.
     */
    data class DocumentPath(val pathSegments: List<String>) : FirestorePath() {
        init {
            require(pathSegments.isNotEmpty()) { "Document path should not be empty" }
        }

        override fun toString(): String = pathSegments.joinToString("/")

        /**
         * Checks if the document ID is auto-generated based on the path segments.
         *
         * @return `true` if the document ID is auto-generated, `false` otherwise.
         */
        fun isDocIdAutoGenerated(): Boolean = pathSegments.size % 2 != 0
    }
}
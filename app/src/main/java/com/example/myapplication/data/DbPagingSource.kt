package com.example.myapplication.data


//private const val UNSPLASH_STARTING_PAGE_INDEX = 1
//
//class DbPagingSource(
//    private val db: AppDatabase,
//    private val sourceType: SourceType
//) : PagingSource<Int, ImageEntity>() {
//
//    val dao = db.imageDao()
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
//        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
//
//        return try {
//            val response = dao.
//            val images = response.results
//
//            LoadResult.Page(
//                data = photos,
//                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
//                nextKey = if (photos.isEmpty()) null else position + 1
//            )
//        } catch (exception: IOException) {
//            LoadResult.Error(exception)
//        } catch (exception: HttpException) {
//            LoadResult.Error(exception)
//        }
//    }
//}
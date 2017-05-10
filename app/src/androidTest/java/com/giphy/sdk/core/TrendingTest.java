package com.giphy.sdk.core;

import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.models.enums.RatingType;
import com.giphy.sdk.core.models.enums.RenditionType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.ListMediaResponse;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TrendingTest {
    GPHApiClient imp;

    @Before
    public void setUp() throws Exception {
        imp = new GPHApiClient("dc6zaTOxFJmzC");
    }

    /**
     * Test if trending without params returns 25 gifs and not exception.
     * @throws Exception
     */
    @Test
    public void testBaseGif() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);

        imp.trending(MediaType.gif, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                Assert.assertNull(e);
                Assert.assertNotNull(result);
                Assert.assertTrue(result.getData().size() == 25);

                lock.countDown();
            }
        });
        lock.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * Test if trending without params returns 25 gifs and not exception.
     * @throws Exception
     */
    @Test
    public void testBaseSticker() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);

        imp.trending(MediaType.sticker, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                Assert.assertNull(e);
                Assert.assertNotNull(result);
                Assert.assertTrue(result.getData().size() == 25);

                lock.countDown();
            }
        });
        lock.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * Test if limit returns the exact amount of gifs
     * @throws Exception
     */
    @Test
    public void testLimit() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);

        imp.trending(MediaType.gif, 13, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                Assert.assertNull(e);
                Assert.assertNotNull(result);
                Assert.assertTrue(result.getData().size() == 13);

                lock.countDown();
            }
        });
        lock.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * Test if rating returns gifs
     * @throws Exception
     */
    @Test
    public void testRating() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);

        imp.trending(MediaType.gif, 20, null, RatingType.g, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                Assert.assertNull(e);
                Assert.assertNotNull(result);
                Assert.assertTrue(result.getData().size() == 20);

                lock.countDown();
            }
        });
        lock.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * Test if the 11th gif from the request with offset 0 is the same as the first gif from the
     * request with offset 10
     * @throws Exception
     */
    @Test
    public void testOffset() throws Exception {
        final CountDownLatch lock = new CountDownLatch(2);

        imp.trending(MediaType.gif, 20, 0, RatingType.pg, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(final ListMediaResponse result1, Throwable e) {
                Assert.assertNull(e);
                Assert.assertNotNull(result1);
                Assert.assertTrue(result1.getData().size() == 20);

                imp.trending(MediaType.gif, 20, 10, RatingType.pg, new CompletionHandler<ListMediaResponse>() {
                    @Override
                    public void onComplete(ListMediaResponse result2, Throwable e) {
                        Assert.assertNull(e);
                        Assert.assertNotNull(result2);
                        Assert.assertTrue(result2.getData().size() == 20);

                        Utils.checkOffsetWorks(result1.getData(), result2.getData(), 1);

                        lock.countDown();
                    }
                });

                lock.countDown();
            }
        });
        lock.await(3000, TimeUnit.MILLISECONDS);
    }

    /**
     * Test if pagination is returned.
     * @throws Exception
     */
    @Test
    public void testPagination() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);

        imp.trending(MediaType.gif, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                Assert.assertNull(e);
                Assert.assertNotNull(result);
                Assert.assertTrue(result.getData().size() == 25);

                Assert.assertNotNull(result.getPagination());
                Assert.assertTrue(result.getPagination().getCount() == 25);

                lock.countDown();
            }
        });
        lock.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * Test if meta is returned.
     * @throws Exception
     */
    @Test
    public void testMeta() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);

        imp.trending(MediaType.gif, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                Assert.assertNull(e);
                Assert.assertNotNull(result);
                Assert.assertTrue(result.getData().size() == 25);

                Assert.assertNotNull(result.getMeta());
                Assert.assertTrue(result.getMeta().getStatus() == 200);
                Assert.assertEquals(result.getMeta().getMsg(), "OK");
                Assert.assertNotNull(result.getMeta().getResponseId());

                lock.countDown();
            }
        });
        lock.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * Test boolean fields
     * @throws Exception
     */
    @Test
    public void testBooleanFields() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);
        final GPHApi imp = new GPHApiClient("4OMJYpPoYwVpe");
        imp.trending(MediaType.gif, 90, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                Assert.assertNull(e);
                Assert.assertNotNull(result);
                Assert.assertTrue(result.getData().size() == 90);

                boolean isIndexable = false;
                boolean isUserPublic = false;
                for (Media media : result.getData()) {
                    isIndexable = isIndexable || media.getIsIndexable();
                    if (media.getUser() != null) {
                        isUserPublic = isUserPublic || media.getUser().getIsPublic();
                    }
                }
                Assert.assertTrue(isIndexable);
                Assert.assertTrue(isUserPublic);
                lock.countDown();
            }
        });
        lock.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * Test renditions and media id
     * @throws Exception
     */
    @Test
    public void testRenditionsAndMediaId() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);
        final GPHApi imp = new GPHApiClient("4OMJYpPoYwVpe");
        imp.trending(MediaType.gif, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                Assert.assertNull(e);
                Assert.assertNotNull(result);
                Assert.assertTrue(result.getData().size() == 25);

                for (Media media : result.getData()) {
                    if (media.getImages().getOriginal() != null) {
                        Assert.assertEquals(media.getImages().getOriginal().getMediaId(), media.getId());
                        Assert.assertTrue(media.getImages().getOriginal().getRenditionType() == RenditionType.original);
                    }
                    if (media.getImages().getPreview() != null) {
                        Assert.assertEquals(media.getImages().getPreview().getMediaId(), media.getId());
                        Assert.assertTrue(media.getImages().getPreview().getRenditionType() == RenditionType.preview);
                    }
                    if (media.getImages().getFixedWidth() != null) {
                        Assert.assertEquals(media.getImages().getFixedWidth().getMediaId(), media.getId());
                        Assert.assertTrue(media.getImages().getFixedWidth().getRenditionType() == RenditionType.fixedWidth);
                    }
                    if (media.getImages().getFixedHeight() != null) {
                        Assert.assertEquals(media.getImages().getFixedHeight().getMediaId(), media.getId());
                        Assert.assertTrue(media.getImages().getFixedHeight().getRenditionType() == RenditionType.fixedHeight);
                    }
                    if (media.getImages().getOriginalStill() != null) {
                        Assert.assertEquals(media.getImages().getOriginalStill().getMediaId(), media.getId());
                        Assert.assertTrue(media.getImages().getOriginalStill().getRenditionType() == RenditionType.originalStill);
                    }
                }
                lock.countDown();
            }
        });
        lock.await(2000, TimeUnit.MILLISECONDS);
    }
}
package com.mhp.coding.challenges.mapping.mappers;

import com.mhp.coding.challenges.mapping.models.db.Article;
import com.mhp.coding.challenges.mapping.models.db.Image;
import com.mhp.coding.challenges.mapping.models.db.blocks.ArticleBlock;
import com.mhp.coding.challenges.mapping.models.db.blocks.GalleryBlock;
import com.mhp.coding.challenges.mapping.models.db.blocks.ImageBlock;
import com.mhp.coding.challenges.mapping.models.db.blocks.TextBlock;
import com.mhp.coding.challenges.mapping.models.db.blocks.VideoBlock;
import com.mhp.coding.challenges.mapping.models.dto.ArticleDto;
import com.mhp.coding.challenges.mapping.models.dto.ImageDto;
import com.mhp.coding.challenges.mapping.models.dto.blocks.ArticleBlockDto;
import com.mhp.coding.challenges.mapping.models.dto.blocks.GalleryBlockDto;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public ArticleDto map(Article article) {
        ArticleDto articleDto = new ArticleDto();
        BeanUtils.copyProperties(article, articleDto, "blocks");
        Comparator<ArticleBlockDto> bySortIndex = Comparator.comparing(ArticleBlockDto::getSortIndex);
        List<ArticleBlockDto> articleBlockDtoList = article.getBlocks().stream()
                .map(this::mapArticleBlockDto)
                .sorted(bySortIndex)
                .collect(Collectors.toList());
        articleDto.setBlocks(articleBlockDtoList);
        return articleDto;
    }

    public List<ArticleDto> map(List<Article> articles) {
        return articles.stream().map(this::map).collect(Collectors.toList());
    }

    public Article map(ArticleDto articleDto) {
        // Nicht Teil dieser Challenge.
        return new Article();
    }

    private ArticleBlockDto mapArticleBlockDto(ArticleBlock block) {
        if (block instanceof GalleryBlock) {
            return mapGalleryBlockDto((GalleryBlock) block);
        }
        if (block instanceof ImageBlock) {
            return mapImageBlockDto((ImageBlock) block);
        }
        if (block instanceof TextBlock) {
            return mapTextBlockDto((TextBlock) block);
        }
        if (block instanceof VideoBlock) {
            return mapVideoBlockDto((VideoBlock) block);
        }
        throw new IllegalStateException("Unknown article block type [" + block.getClass().getName() + "]");
    }

    private ArticleBlockDto mapGalleryBlockDto(GalleryBlock galleryBlock) {
        GalleryBlockDto galleryBlockDto = new GalleryBlockDto();
        BeanUtils.copyProperties(galleryBlock, galleryBlockDto, "images");
        List<ImageDto> imageDtoList = galleryBlock.getImages().stream()
                .filter(Objects::nonNull)
                .map(this::mapImageDto)
                .collect(Collectors.toList());
        galleryBlockDto.setImages(imageDtoList);
        return galleryBlockDto;
    }

    private ArticleBlockDto mapImageBlockDto(ImageBlock imageBlock) {
        com.mhp.coding.challenges.mapping.models.dto.blocks.ImageBlock imageBlockDto =
                new com.mhp.coding.challenges.mapping.models.dto.blocks.ImageBlock();
        BeanUtils.copyProperties(imageBlock, imageBlockDto, "image");
        imageBlockDto.setImage(mapImageDto(imageBlock.getImage()));
        return imageBlockDto;
    }

    private ArticleBlockDto mapTextBlockDto(TextBlock textBlock) {
        com.mhp.coding.challenges.mapping.models.dto.blocks.TextBlock textBlockDto =
                new com.mhp.coding.challenges.mapping.models.dto.blocks.TextBlock();
        BeanUtils.copyProperties(textBlock, textBlockDto);
        return textBlockDto;
    }

    private ArticleBlockDto mapVideoBlockDto(VideoBlock videoBlock) {
        com.mhp.coding.challenges.mapping.models.dto.blocks.VideoBlock videoBlockDto =
                new com.mhp.coding.challenges.mapping.models.dto.blocks.VideoBlock();
        BeanUtils.copyProperties(videoBlock, videoBlockDto);
        return videoBlockDto;
    }

    private ImageDto mapImageDto(Image image) {
        if (image == null) {
            return null;
        }
        ImageDto imageDto = new ImageDto();
        BeanUtils.copyProperties(image, imageDto);
        return imageDto;
    }
}

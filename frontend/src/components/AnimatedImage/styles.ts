import styled from '@emotion/styled'

import { ImageVariant } from '@/types/ui'

export const AnimatedImageWrapper = styled.div<{ variant: ImageVariant }>`
  ${({ variant }) => {
    switch (variant) {
      case ImageVariant.pop:
        return `
          transform: scale(0.8);
          opacity: 0;
          animation: popIn 0.1s ease-out forwards;

          @keyframes popIn {
            0% {
              opacity: 0;
              transform: scale(0.8);
            }
            100% {
              opacity: 1;
              transform: scale(1);
            }
          }
        `
      case ImageVariant.slice:
        return `
           transform: translateX(-30px);  
           opacity: 0;
           animation: carouselSlideIn 0.5s cubic-bezier(0.22, 1, 0.36, 1) forwards;
          @keyframes carouselSlideIn {
            0% {
              opacity: 0;
              transform: translateX(-30px);
            }
            100% {
              opacity: 1;
              transform: translateX(0);
            }
          }
        `
      case ImageVariant.fade:
        return `
          opacity: 0;
          animation: fadeIn 0.5s ease-out forwards;

          @keyframes fadeIn {
            0% { opacity: 0; }
            100% { opacity: 1; }
          }
        `
      case ImageVariant.slideLeft:
        return `
          transform: translateX(20px);
          opacity: 0;
          animation: slideLeft 0.3s ease-out forwards;

          @keyframes slideLeft {
            0% {
              opacity: 0;
              transform: translateX(20px);
            }
            100% {
              opacity: 1;
              transform: translateX(0);
            }
          }
        `
      case ImageVariant.slideRight:
        return `
          transform: translateX(-20px);
          opacity: 0;
          animation: slideRight 0.3s ease-out forwards;

          @keyframes slideRight {
            0% {
              opacity: 0;
              transform: translateX(-20px);
            }
            100% {
              opacity: 1;
              transform: translateX(0);
            }
          }
        `
      case ImageVariant.slideUp:
        return `
          transform: translateY(20px);
          opacity: 0;
          animation: slideUp 0.3s ease-out forwards;

          @keyframes slideUp {
            0% {
              opacity: 0;
              transform: translateY(20px);
            }
            100% {
              opacity: 1;
              transform: translateY(0);
            }
          }
        `
      case ImageVariant.slideDown:
        return `
          transform: translateY(-20px);
          opacity: 0;
          animation: slideDown 0.3s ease-out forwards;

          @keyframes slideDown {
            0% {
              opacity: 0;
              transform: translateY(-20px);
            }
            100% {
              opacity: 1;
              transform: translateY(0);
            }
          }
        `
      case ImageVariant.zoomIn:
        return `
          transform: scale(0.5);
          opacity: 0;
          animation: zoomIn 0.3s ease-out forwards;

          @keyframes zoomIn {
            0% {
              opacity: 0;
              transform: scale(0.5);
            }
            100% {
              opacity: 1;
              transform: scale(1);
            }
          }
        `
      case ImageVariant.zoomOut:
        return `
          transform: scale(1.5);
          opacity: 0;
          animation: zoomOut 0.3s ease-out forwards;

          @keyframes zoomOut {
            0% {
              opacity: 0;
              transform: scale(1.5);
            }
            100% {
              opacity: 1;
              transform: scale(1);
            }
          }
        `
      case ImageVariant.flip:
        return `
          transform: rotateY(90deg);
          opacity: 0;
          animation: flip 0.5s ease-out forwards;

          @keyframes flip {
            0% {
              opacity: 0;
              transform: rotateY(90deg);
            }
            100% {
              opacity: 1;
              transform: rotateY(0);
            }
          }
        `
      case ImageVariant.bounce:
        return `
          transform: translateY(0);
          opacity: 0;
          animation: bounce 0.5s ease-out forwards;

          @keyframes bounce {
            0% {
              opacity: 0;
              transform: translateY(-20px);
            }
            50% {
              opacity: 1;
              transform: translateY(10px);
            }
            100% {
              opacity: 1;
              transform: translateY(0);
            }
          }
        `
      case ImageVariant.rotate:
        return `
          transform: rotate(-180deg);
          opacity: 0;
          animation: rotate 0.5s ease-out forwards;

          @keyframes rotate {
            0% {
              opacity: 0;
              transform: rotate(-180deg);
            }
            100% {
              opacity: 1;
              transform: rotate(0);
            }
          }
        `
      case ImageVariant.blur:
        return `
          filter: blur(10px);
          opacity: 0;
          animation: blur 0.5s ease-out forwards;

          @keyframes blur {
            0% {
              opacity: 0;
              filter: blur(10px);
            }
            100% {
              opacity: 1;
              filter: blur(0);
            }
          }
        `
      case ImageVariant.scale:
        return `
          transform: scale(0.5);
          opacity: 0;
          animation: scale 0.5s ease-out forwards;

          @keyframes scale {
            0% {
              opacity: 0;
              transform: scale(0.5);
            }
            50% {
              opacity: 1;
              transform: scale(1.2);
            }
            100% {
              opacity: 1;
              transform: scale(1);
            }
          }
        `
      case ImageVariant.sliceFade:
        return `
          transform: translateY(-20px);
          opacity: 0;
          animation: sliceFade 0.5s ease-out forwards;

          @keyframes sliceFade {
            0% {
              opacity: 0;
              transform: translateY(-20px);
            }
            50% {
              opacity: 0.5;
              transform: translateY(-10px);
            }
            100% {
              opacity: 1;
              transform: translateY(0);
            }
          }
        `
      default:
        return ''
    }
  }}
`

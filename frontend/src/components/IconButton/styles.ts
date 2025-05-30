/** @jsxImportSource @emotion/react */
import styled from '@emotion/styled'

export const StyledIconButton = styled.div`
  all: unset;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: opacity 0.2s ease-in-out;
  user-select: none;

  &:hover {
    opacity: 0.6;
  }

  &:active {
    opacity: 0.4;
  }

  &:focus {
    opacity: 1;
  }
`

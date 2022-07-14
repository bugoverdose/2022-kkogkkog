import { css } from '@emotion/react';
import styled from '@emotion/styled';

export const Root = styled.div<{ hasCursor?: boolean }>`
  width: 100%;
  max-width: 380px;
  min-width: 125px;

  display: flex;
  justify-content: space-between;
  align-items: center;

  padding-left: 15px;

  border-radius: 4px;
  box-shadow: 0 4px 4px 0 #00000025;

  aspect-ratio: 3/1;

  ${({ theme }) => css`
    background-color: ${theme.colors.white_100};
  `}

  ${({ hasCursor }) =>
    hasCursor &&
    css`
      cursor: pointer;
    `}
`;

export const TextContainer = styled.div`
  overflow-x: scroll;

  width: 70%;

  font-size: 16px;
  font-weight: 700;
  line-height: 40px;

  white-space: nowrap;

  @media (max-width: 400px) {
    font-size: 16px;
  }

  @media (max-width: 360px) {
    font-size: 14px;
  }
`;

export const TypeText = styled.span`
  text-decoration: underline 2px;
`;

export const ImageContainer = styled.div<{ backgroundColor: string }>`
  width: 30%;
  height: 100%;

  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 0 4px 4px 0;
  background: ${({ backgroundColor }) => backgroundColor};

  & > img {
    border-radius: 50%;
    width: 80%;
    box-shadow: 0 4px 4px 0 #00000025;
  }
`;

export const LinkButtonContainer = styled.div`
  font-size: 32px;

  text-align: center;
  width: 100%;
`;

export const LinkButtonText = styled.div`
  font-size: 14px;
`;

import { useMemo } from 'react';

import ListFilter from '@/@components/@shared/ListFilter';
import KkogKkogList from '@/@components/kkogkkog/KkogKkogList';
import { SentKkogKkogFilterOptionType } from '@/@components/kkogkkog/SentKkogKkog';
import { useStatus } from '@/@hooks/@common/useStatus';
import { KkogKKogResponse } from '@/types/remote/response';

import * as Styled from './style';

interface ReceivedKkogKkogProps {
  kkogkkogList: KkogKKogResponse[] | undefined;
}

const filterOption = ['요청', '대기'] as const;

export type ReceivedKkogKkogFilterOptionType = typeof filterOption[number];

const ReceivedKkogKkog = (props: ReceivedKkogKkogProps) => {
  const { kkogkkogList } = props;

  const { status, changeStatus } = useStatus<SentKkogKkogFilterOptionType>('요청');

  const onClickFilterButton = (status: SentKkogKkogFilterOptionType) => {
    changeStatus(status);
  };

  const parsedKkogKkogList = useMemo(
    () =>
      kkogkkogList?.reduce(
        (prev, kkogkkog) => {
          const key = kkogkkog.couponStatus;

          return { ...prev, [key]: [...prev[key], kkogkkog] };
        },
        {
          REQUESTED: [],
          READY: [],
        } as any
      ),
    [kkogkkogList]
  );

  return (
    <Styled.Root>
      <ListFilter<SentKkogKkogFilterOptionType>
        status={status}
        onClickFilterButton={onClickFilterButton}
        options={filterOption}
      />
      {status === '요청' && (
        <Styled.Container>
          <div>
            사용 <span>요청을 한</span> 꼭꼭
          </div>
          <KkogKkogList kkogkkogList={parsedKkogKkogList['REQUESTED']} />
        </Styled.Container>
      )}

      {status === '대기' && (
        <Styled.Container>
          <div>
            사용 <span>하지 않은</span> 꼭꼭
          </div>
          <KkogKkogList kkogkkogList={parsedKkogKkogList['READY']} />
        </Styled.Container>
      )}
    </Styled.Root>
  );
};

export default ReceivedKkogKkog;
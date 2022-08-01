import { SVGProps } from 'react';

const Plus = (props: SVGProps<SVGSVGElement>) => {
  return (
    <svg
      width='33'
      height='33'
      viewBox='0 0 33 33'
      fill='none'
      xmlns='http://www.w3.org/2000/svg'
      {...props}
    >
      <path
        fillRule='evenodd'
        clipRule='evenodd'
        d='M16.5 1.375C8.14687 1.375 1.375 8.14687 1.375 16.5C1.375 24.8531 8.14687 31.625 16.5 31.625C24.8531 31.625 31.625 24.8531 31.625 16.5C31.625 8.14687 24.8531 1.375 16.5 1.375ZM17.875 22C17.875 22.3647 17.7301 22.7144 17.4723 22.9723C17.2144 23.2301 16.8647 23.375 16.5 23.375C16.1353 23.375 15.7856 23.2301 15.5277 22.9723C15.2699 22.7144 15.125 22.3647 15.125 22V17.875H11C10.6353 17.875 10.2856 17.7301 10.0277 17.4723C9.76987 17.2144 9.625 16.8647 9.625 16.5C9.625 16.1353 9.76987 15.7856 10.0277 15.5277C10.2856 15.2699 10.6353 15.125 11 15.125H15.125V11C15.125 10.6353 15.2699 10.2856 15.5277 10.0277C15.7856 9.76987 16.1353 9.625 16.5 9.625C16.8647 9.625 17.2144 9.76987 17.4723 10.0277C17.7301 10.2856 17.875 10.6353 17.875 11V15.125H22C22.3647 15.125 22.7144 15.2699 22.9723 15.5277C23.2301 15.7856 23.375 16.1353 23.375 16.5C23.375 16.8647 23.2301 17.2144 22.9723 17.4723C22.7144 17.7301 22.3647 17.875 22 17.875H17.875V22Z'
        fill='white'
      />
    </svg>
  );
};

export default Plus;
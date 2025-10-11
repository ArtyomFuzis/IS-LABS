import type { ReactElement } from 'react';
import ReactDOM from 'react-dom';
import "../../Styles/Frames.css"


function BaseFrame({ children, isOpen, onClose }: {children: ReactElement, isOpen:boolean, onClose: () => void}) {
  if (!isOpen) return null;

  return ReactDOM.createPortal(
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        {children}
      </div>
    </div>,
    document.getElementById('modal-root')!
  );
}

export default BaseFrame
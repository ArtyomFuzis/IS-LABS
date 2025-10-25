import React, { useState } from 'react';
import BaseFrame from '../BaseFrame';


function DeleteAlert({onClose}: {onClose: () => void}) {
  return (
        <>
            <div className="modal-main-content">
              <div className="modal-header">Ошибка удаления</div>
                <div className="modal-params-container">
                    При попытке удаления произошла ошибка, проверьте отсутствие связанных объектов
                </div> 
            </div>
            <div className="modal-buttons">
                <button className="modal-close-button" onClick={onClose}>Закрыть</button>
            </div>
        </>
    )
};

export default DeleteAlert;
function ErrorPanel({errorMessages} : {errorMessages: string[]}){
    return (
        <>
        {errorMessages.length != 0 && 
                <div className="modal-params-error-pane">
                    <div className="modal-params-error-pane-header">
                        <img className="modal-params-error-pane-header-img" src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/7a/Codex_icon_error_color-error.svg/640px-Codex_icon_error_color-error.svg.png"/>
                        <div className="modal-params-error-pane-header-title">Ошибки ввода</div>
                    </div>
                    <div className="modal-params-error-pane-body">
                        <ul>
                            {errorMessages.map(item => (
                                <li key={item} className="modal-params-error-pane-row">{item}</li>
                            ))}
                        </ul>
                    </div>
                </div>
            }
        </>

        
    )
}
export default ErrorPanel
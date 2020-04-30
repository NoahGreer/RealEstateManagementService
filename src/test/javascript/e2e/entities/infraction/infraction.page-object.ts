import { element, by, ElementFinder } from 'protractor';

export class InfractionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-infraction div table .btn-danger'));
  title = element.all(by.css('jhi-infraction div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class InfractionUpdatePage {
  pageTitle = element(by.id('jhi-infraction-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  dateOccurredInput = element(by.id('field_dateOccurred'));
  causeInput = element(by.id('field_cause'));
  resolutionInput = element(by.id('field_resolution'));

  leaseSelect = element(by.id('field_lease'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setDateOccurredInput(dateOccurred: string): Promise<void> {
    await this.dateOccurredInput.sendKeys(dateOccurred);
  }

  async getDateOccurredInput(): Promise<string> {
    return await this.dateOccurredInput.getAttribute('value');
  }

  async setCauseInput(cause: string): Promise<void> {
    await this.causeInput.sendKeys(cause);
  }

  async getCauseInput(): Promise<string> {
    return await this.causeInput.getAttribute('value');
  }

  async setResolutionInput(resolution: string): Promise<void> {
    await this.resolutionInput.sendKeys(resolution);
  }

  async getResolutionInput(): Promise<string> {
    return await this.resolutionInput.getAttribute('value');
  }

  async leaseSelectLastOption(): Promise<void> {
    await this.leaseSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async leaseSelectOption(option: string): Promise<void> {
    await this.leaseSelect.sendKeys(option);
  }

  getLeaseSelect(): ElementFinder {
    return this.leaseSelect;
  }

  async getLeaseSelectedOption(): Promise<string> {
    return await this.leaseSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class InfractionDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-infraction-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-infraction'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
